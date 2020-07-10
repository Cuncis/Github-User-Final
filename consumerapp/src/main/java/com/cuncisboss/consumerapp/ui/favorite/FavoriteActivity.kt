package com.cuncisboss.consumerapp.ui.favorite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.consumerapp.R
import com.cuncisboss.consumerapp.adapter.FavoriteAdapter
import com.cuncisboss.consumerapp.data.local.FavoriteContract.FavoriteColoums.Companion.CONTENT_URI
import com.cuncisboss.consumerapp.data.model.FavoriteModel
import com.cuncisboss.consumerapp.ui.detail.DetailUserActivity
import com.cuncisboss.consumerapp.ui.setting.SettingActivity
import com.cuncisboss.consumerapp.util.Constants.EXTRA_FAV_NAME
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity(), FavoriteAdapter.FavoriteClickListener {
    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        dataObserver(savedInstanceState)

        adapter = FavoriteAdapter(this)
        rv_favorites.adapter = adapter

        observeViewModel()
    }

    private fun dataObserver(savedInstanceState: Bundle?) {
        val handledThread = HandlerThread("DataObserver")
        handledThread.start()
        val handler = Handler(handledThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                favoriteViewModel.getAllFavorites()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            favoriteViewModel.getAllFavorites()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteModel>(EXTRA_STATE)
            if (list != null) {
                adapter.favList = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.favList)
    }

    private fun observeViewModel() {
        favoriteViewModel.favoriteList.observe(this, Observer {
            if (it.isNotEmpty()) {
                if (it.isNotEmpty()) {
                    adapter.favList = it as ArrayList<FavoriteModel>
                } else {
                    adapter.favList = ArrayList()
                }
            } else {
                Toast.makeText(this, "Favorites is Empty", Toast.LENGTH_SHORT).show()
            }
        })
        favoriteViewModel.removeFavorite.observe(this, Observer {
            if (it > 0) {
                Toast.makeText(this, "Favorite Removed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Favorite failed to remove", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.github_menu, menu)
        val menuFav = menu?.findItem(R.id.action_favorites)
        menuFav?.isVisible = false
        return true
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

    override fun onItemClick(favModel: FavoriteModel, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(favModel.name)
        builder.setPositiveButton("Detail") { dialog, _ ->
            val intent = Intent(this, DetailUserActivity::class.java)
            intent.putExtra(EXTRA_FAV_NAME, favModel.name)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("Remove") { dialog, _ ->
            favoriteViewModel.removeFavorite(favModel.id.toString())
            adapter.removeFavoriteList(position)
            dialog.dismiss()
        }
        builder.show()

    }
}