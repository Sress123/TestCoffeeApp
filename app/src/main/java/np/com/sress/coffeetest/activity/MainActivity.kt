package np.com.sress.coffeetest.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import np.com.sress.coffeetest.R
import np.com.sress.coffeetest.adapter.BannerAdapter
import np.com.sress.coffeetest.adapter.CategoryAdapter
import np.com.sress.coffeetest.adapter.PopularAdapter
import np.com.sress.coffeetest.database.InternalDatabase
import np.com.sress.coffeetest.database.InternalDatabase.Companion.TABLE_POPULAR
import np.com.sress.coffeetest.databinding.ActivityMainBinding
import np.com.sress.coffeetest.model.BannerModel
import np.com.sress.coffeetest.model.CategoryModel
import np.com.sress.coffeetest.model.PopularModel
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var bannerAdapter: BannerAdapter
    private val sqlt_in: InternalDatabase by lazy { InternalDatabase(applicationContext) }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()

        val transformer = androidx.viewpager2.widget.CompositePageTransformer()

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin) // e.g. 16dp in dimens.xml
        transformer.addTransformer(androidx.viewpager2.widget.MarginPageTransformer(pageMarginPx))

        transformer.addTransformer { page, position ->
            val scale = 0.85f + (1 - abs(position)) * 0.15f
            page.scaleY = scale
        }

        binding.bannerViewPager.setPageTransformer(transformer)


    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE

        val bannerUrls = mutableListOf<String>()
        val db = sqlt_in.readableDatabase
        val cursor = db.rawQuery("SELECT url FROM ${InternalDatabase.TABLE_BANNER}", null)

        if (cursor.moveToFirst()) {
            do {
                val url = cursor.getString(cursor.getColumnIndexOrThrow("url"))
                bannerUrls.add(url)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        bannerAdapter = BannerAdapter(bannerUrls)
        binding.bannerViewPager.adapter = bannerAdapter

        binding.progressBarBanner.visibility = View.GONE

        autoSlideBanner()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        val db = sqlt_in.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, title FROM ${InternalDatabase.TABLE_CATEGORY} ORDER BY id ASC",
            null
        )

        val categoryList = mutableListOf<CategoryModel>()
        if (cursor.moveToFirst()) {
            do {
                val model = CategoryModel().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                }
                categoryList += model
            } while (cursor.moveToNext())
        }
        cursor.close()
        binding.progressBarCategory.visibility = View.GONE

        val adapter = CategoryAdapter(categoryList)
        binding.recycleViewCat.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        val popularItems = mutableListOf<PopularModel>()

        val db = sqlt_in.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_POPULAR", null)

        if (cursor.moveToFirst()) {
            do {
                val item = PopularModel().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    extra = cursor.getString(cursor.getColumnIndexOrThrow("extra"))
                    val rawUrls = cursor.getString(cursor.getColumnIndexOrThrow("picUrl"))
                    picUrl = rawUrls.split(",").firstOrNull()?.trim() ?: ""
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                    rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"))
                }
                popularItems.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()

        binding.recycleViewPopular.layoutManager = GridLayoutManager(this, 2)
        binding.recycleViewPopular.adapter = PopularAdapter(popularItems)
        binding.progressBarPopular.visibility = View.GONE
    }

    private fun autoSlideBanner() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val itemCount = bannerAdapter.itemCount
                if (itemCount == 0) return

                val nextItem = (binding.bannerViewPager.currentItem + 1) % itemCount
                binding.bannerViewPager.currentItem = nextItem
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}
