package com.cuncisboss.githubuserfinal.data.local.db

import android.provider.BaseColumns

class FavoriteContract {
    class FavoriteColoums: BaseColumns {
        companion object {
            val TABLE_NAME = "favorite_table"
            val COLUMN_NAME = "name"
            val COLUMN_IMAGE = "image"
            val IS_FAVORITE = "favorite"
        }
    }
}