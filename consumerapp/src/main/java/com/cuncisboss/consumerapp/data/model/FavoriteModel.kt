package com.cuncisboss.consumerapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FavoriteModel(
    var name: String = "",
    var image: String = "",
    var isFavorite: Int = 0,
    var id: Int = 0
): Parcelable