package com.cuncisboss.githubuserfinal.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteHelper
import com.cuncisboss.githubuserfinal.data.local.db.MappingHelper
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

    init {
        dbHelper.open()
    }

    fun getAllFavorites() {
        viewModelScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = dbHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            _favoriteList.postValue(notes)
        }
    }

    fun removeFavorite(username: String) {
        viewModelScope.launch {
            try {
                val rm = dbHelper.deleteByUsername(username)
                _removeFavorite.postValue(rm)
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