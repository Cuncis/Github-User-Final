package com.cuncisboss.githubuserfinal.data.local.db

import android.net.Uri
import android.provider.BaseColumns

object FavoriteContract {

    const val AUTHORITY = "com.cuncisboss.githubuserfinal"
    const val SCHEME = "content"

    class FavoriteColoums: BaseColumns {
        companion object {
            val TABLE_NAME = "favorite_table"
            val COLUMN_NAME = "name"
            val COLUMN_IMAGE = "image"
            val IS_FAVORITE = "favorite"

            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}