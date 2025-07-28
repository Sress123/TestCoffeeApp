package np.com.sress.coffeetest.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import np.com.sress.coffeetest.R
import np.com.sress.coffeetest.adapter.ItemListCategoryAdapter
import np.com.sress.coffeetest.database.InternalDatabase
import np.com.sress.coffeetest.database.InternalDatabase.Companion.TABLE_ITEMS
import np.com.sress.coffeetest.database.InternalDatabase.Companion.TABLE_POPULAR
import np.com.sress.coffeetest.databinding.ActivityItemListBinding
import np.com.sress.coffeetest.model.BannerModel
import np.com.sress.coffeetest.model.ItemsModel

class ItemListActivity : AppCompatActivity() {

    private val sqlt_in: InternalDatabase by lazy { InternalDatabase(applicationContext) }
    lateinit var binding: ActivityItemListBinding
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        initList()

        binding.backBtn.setOnClickListener {finish()}
    }

    private fun initList() {
        binding.progressBar.visibility = View.VISIBLE

        val itemList = mutableListOf<ItemsModel>()
        val db = sqlt_in.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ITEMS WHERE categoryId = ?", arrayOf(id))

        if (cursor.moveToFirst()) {
            do {
                val item = ItemsModel().apply {
                    this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    this.categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"))
                    this.title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                    this.description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    this.extra = cursor.getString(cursor.getColumnIndexOrThrow("extra"))
                    this.picUrl = listOf(cursor.getString(cursor.getColumnIndexOrThrow("picUrl")))
                    this.price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                    this.rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"))
                }
                itemList.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()

        binding.listView.layoutManager = LinearLayoutManager(this)
        binding.listView.adapter = ItemListCategoryAdapter(itemList)

        binding.progressBar.visibility = View.GONE
    }



    private fun getBundle() {
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!

        binding.categoryTxt.text = title
    }
}