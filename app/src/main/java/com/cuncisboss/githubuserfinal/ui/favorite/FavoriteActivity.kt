package com.cuncisboss.githubuserfinal.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteHelper
import com.cuncisboss.githubuserfinal.data.local.db.MappingHelper
import com.cuncisboss.githubuserfinal.ui.setting.SettingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var dbHelper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dbHelper = FavoriteHelper.getInstance(this)
        dbHelper.open()

        loadFavoriteAsync()
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
//            progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = dbHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
//            progressbar.visibility = View.INVISIBLE
//            val notes = deferredNotes.await()
//            if (notes.size > 0) {
//                adapter.listNotes = notes
//            } else {
//                adapter.listNotes = ArrayList()
//                showSnackbarMessage("Tidak ada data saat ini")
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.github_menu, menu)
        val menuFav = menu?.findItem(R.id.action_favorites)
        menuFav?.isVisible = false
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}