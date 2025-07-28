package np.com.sress.coffeetest.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InternalDatabase(context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null, KEY_DATABASE_VERSION) {

    companion object {
        private const val KEY_DATABASE_VERSION = 1
        private const val DATABASE_NAME = "coffee_data.db"
        const val TABLE_BANNER = "tbl_banner"
        const val TABLE_CATEGORY = "tbl_category"
        const val TABLE_ITEMS = "tbl_items"
        const val TABLE_POPULAR = "tbl_popular"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_BANNER (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "url TEXT NOT NULL)"
            )

            db?.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORY (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL)"
            )

            db?.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_ITEMS (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "categoryId INTEGER NOT NULL, " +
                        "description TEXT, " +
                        "extra TEXT, " +
                        "picUrl TEXT, " +
                        "price REAL, " +
                        "rating REAL, " +
                        "title TEXT NOT NULL, " +
                        "FOREIGN KEY (categoryId) REFERENCES $TABLE_CATEGORY(id))"
            )

            db?.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_POPULAR (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "description TEXT, " +
                        "extra TEXT, " +
                        "picUrl TEXT, " +
                        "price REAL, " +
                        "rating REAL)"
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}