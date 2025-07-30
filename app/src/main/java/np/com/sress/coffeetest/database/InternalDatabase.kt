package np.com.sress.coffeetest.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import np.com.sress.coffeetest.model.UserModel

class InternalDatabase(context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null, KEY_DATABASE_VERSION) {

    companion object {
        private const val KEY_DATABASE_VERSION = 1
        private const val DATABASE_NAME = "coffee_data.db"
        const val TABLE_BANNER = "tbl_banner"
        const val TABLE_CATEGORY = "tbl_category"
        const val TABLE_ITEMS = "tbl_items"
        const val TABLE_POPULAR = "tbl_popular"
        const val TABLE_USER = "tbl_user"
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

            db?.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_USER (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "fullName TEXT NOT NULL, " +
                        "phoneNumber TEXT, " +
                        "password TEXT)"
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getUserFromDatabase(tableName: String): ArrayList<UserModel> {
        val ol = ArrayList<UserModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $tableName", null)

        if (cursor.moveToFirst()) {
            do {
                val user = UserModel()
                user.fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"))
                user.phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                user.password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                ol.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return ol
    }
}
