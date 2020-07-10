package com.cuncisboss.githubuserfinal.data.local

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.COLUMN_IMAGE
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.COLUMN_NAME
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.IS_FAVORITE
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<FavoriteModel> {
        val favoriteList = arrayListOf<FavoriteModel>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val image = getString(getColumnIndexOrThrow(COLUMN_IMAGE))
                val isFavorite = getInt(getColumnIndexOrThrow(IS_FAVORITE))
                favoriteList.add(FavoriteModel(name, image, isFavorite, id))
            }
        }

        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor?): FavoriteModel {
        var favorite = FavoriteModel()
        favoriteCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
            val image = getString(getColumnIndexOrThrow(COLUMN_IMAGE))
            val isFavorite = getInt(getColumnIndexOrThrow(IS_FAVORITE))
            favorite = FavoriteModel(name, image, isFavorite, id)
        }

        return favorite
    }

}