package np.com.sress.coffeetest.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import np.com.sress.coffeetest.R
import np.com.sress.coffeetest.database.InternalDatabase
import np.com.sress.coffeetest.databinding.ActivitySplashBinding
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    private lateinit var dbHelper: InternalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = InternalDatabase(this)

        val prefs = getSharedPreferences("init_prefs", MODE_PRIVATE)
        val isDataInserted = prefs.getBoolean("is_data_inserted", false)
        val isUserLoggedIn = prefs.getBoolean("is_user_logged_in", false)


        if (!isDataInserted) {
            readAndInsertJson()
            prefs.edit().putBoolean("is_data_inserted", true).apply()
        }

        binding.startBtn.setOnClickListener {
            if (isUserLoggedIn) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            }
            finish()
        }
    }

    private fun readAndInsertJson() {
        val db = dbHelper.writableDatabase
        try {
            val jsonString = assets.open("database.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)

            // Insert banners
            val banners = jsonObject.getJSONArray("Banner")
            for (i in 0 until banners.length()) {
                val banner = banners.getJSONObject(i)
                val values = ContentValues()
                values.put("url", banner.getString("url"))
                db.insert(InternalDatabase.TABLE_BANNER, null, values)
            }

            // Insert categories
            val categories = jsonObject.getJSONArray("Category")
            for (i in 0 until categories.length()) {
                val category = categories.getJSONObject(i)
                val values = ContentValues()
                values.put("id", category.getInt("id"))
                values.put("title", category.getString("title"))
                db.insert(InternalDatabase.TABLE_CATEGORY, null, values)
            }

            // Insert popular items
            val popular = jsonObject.getJSONArray("Popular")
            for (i in 0 until popular.length()) {
                val item = popular.getJSONObject(i)
                val values = ContentValues()
                values.put("title", item.getString("title"))
                values.put("description", item.getString("description"))
                values.put("extra", item.getString("extra"))
                values.put("picUrl", item.getJSONArray("picUrl").getString(0))
                values.put("price", item.getDouble("price"))
                values.put("rating", item.getDouble("rating"))
                db.insert(InternalDatabase.TABLE_POPULAR, null, values)
            }

            // Insert general items
            val items = jsonObject.getJSONArray("Items")
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val values = ContentValues()
                values.put("categoryId", item.getInt("categoryId"))
                values.put("title", item.getString("title"))
                values.put("description", item.getString("description"))
                values.put("extra", item.getString("extra"))
                values.put("picUrl", item.getJSONArray("picUrl").getString(0))
                values.put("price", item.getDouble("price"))
                values.put("rating", item.getDouble("rating"))
                db.insert(InternalDatabase.TABLE_ITEMS, null, values)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading data: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}