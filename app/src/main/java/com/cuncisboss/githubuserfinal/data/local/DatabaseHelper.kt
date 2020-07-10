package com.cuncisboss.githubuserfinal.data.local


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.COLUMN_IMAGE
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.COLUMN_NAME
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.IS_FAVORITE
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.TABLE_NAME


class DatabaseHelper(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {
    companion object {
        //Jika kamu mengubah skema database maka harus dinaikkan versi databasenya.
        val DATABASE_VERSION = 2
        val DATABASE_NAME = "favorite.db"

        private val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_IMAGE TEXT, " +
                "$IS_FAVORITE INT)"

        private val SQL_DROP_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_ENTRIES)
        onCreate(db)
    }
}