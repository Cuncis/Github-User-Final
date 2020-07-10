package com.cuncisboss.githubuserfinal.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.COLUMN_NAME
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.TABLE_NAME
import java.sql.SQLException


class FavoriteHelper(context: Context) {

    private var dbHelper: DatabaseHelper =
        DatabaseHelper(context)
    private lateinit var db: SQLiteDatabase

    companion object {
        private val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE
                ?: synchronized(this) {
                INSTANCE
                    ?: FavoriteHelper(
                        context
                    )
            }

    }

    @Throws(SQLException::class)
    fun open() {
        db = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()

        if (db.isOpen)
            db.close()
    }

    fun queryAll(): Cursor {
        return db.query(
            DATABASE_TABLE,
        null,
        null,
        null,
        null,
            null,
        null)
    }

    fun queryByUsername(username: String): Cursor {
        return db.rawQuery("SELECT * FROM $DATABASE_TABLE WHERE name=?", arrayOf(username))
    }

    fun insert(values: ContentValues): Long {
        return db.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(name: String): Int {
        return db.delete(DATABASE_TABLE, "$COLUMN_NAME='$name'", null)
    }
}












