package com.example.locationappxx

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LocationDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "locations"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = (
                "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_LATITUDE REAL, " +
                        "$COLUMN_LONGITUDE REAL);"
                )
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLocation(latitude: Double, longitude: Double): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_LATITUDE, latitude)
        values.put(COLUMN_LONGITUDE, longitude)
        val rowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return rowId
    }

    // Aquí puedes agregar métodos adicionales para trabajar con la base de datos
}
