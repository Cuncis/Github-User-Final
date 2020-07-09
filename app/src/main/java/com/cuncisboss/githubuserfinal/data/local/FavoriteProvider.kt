package com.cuncisboss.githubuserfinal.data.local

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.AUTHORITY
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.CONTENT_URI
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.TABLE_NAME
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favoriteHelper.queryAll()
            FAVORITE_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values!!)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d("_log", "delete: ${sUriMatcher.match(uri)}")
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> {
                favoriteHelper.deleteById(uri.lastPathSegment.toString())
            }
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
