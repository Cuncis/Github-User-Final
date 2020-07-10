package com.cuncisboss.githubuserfinal.ui.favorite

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuncisboss.githubuserfinal.data.local.FavoriteContract.FavoriteColoums.Companion.CONTENT_URI
import com.cuncisboss.githubuserfinal.data.local.FavoriteHelper
import com.cuncisboss.githubuserfinal.data.local.MappingHelper
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = FavoriteHelper.getInstance(application)
    private val _favoriteList = MutableLiveData<List<FavoriteModel>>()
    val favoriteList: MutableLiveData<List<FavoriteModel>>
        get() = _favoriteList
    private val _removeFavorite = MutableLiveData<Int>()
    val removeFavorite: MutableLiveData<Int>
        get() = _removeFavorite

    val app = application

    init {
        dbHelper.open()
    }

    fun getAllFavorites() {
        viewModelScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = app.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            _favoriteList.postValue(notes)
        }
    }

    fun removeFavorite(id: String) {
        val uri = Uri.parse("$CONTENT_URI/$id")
        viewModelScope.launch {
            try {
                val cursor = app.contentResolver.delete(uri, null, null)
                _removeFavorite.postValue(cursor)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dbHelper.close()
    }
}