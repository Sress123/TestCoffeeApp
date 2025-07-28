package np.com.sress.coffeetest.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import np.com.sress.coffeetest.adapter.CategoryAdapter
import np.com.sress.coffeetest.adapter.PopularAdapter
import np.com.sress.coffeetest.database.InternalDatabase
import np.com.sress.coffeetest.database.InternalDatabase.Companion.TABLE_POPULAR
import np.com.sress.coffeetest.databinding.ActivityMainBinding
import np.com.sress.coffeetest.model.BannerModel
import np.com.sress.coffeetest.model.CategoryModel
import np.com.sress.coffeetest.model.PopularModel

class MainActivity : AppCompatActivity() {

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
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE

        val db = sqlt_in.readableDatabase
        val cursor = db.rawQuery("SELECT id, url FROM ${InternalDatabase.TABLE_BANNER} LIMIT 1", null)

        if (cursor.moveToFirst()) {
            val bannerModel = BannerModel().apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                url = cursor.getString(cursor.getColumnIndexOrThrow("url"))
            }

            Glide.with(this)
                .load(bannerModel.url)
                .into(binding.banner)
        }

        cursor.close()
        binding.progressBarBanner.visibility = View.GONE
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
}