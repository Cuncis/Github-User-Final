package com.cuncisboss.githubuserfinal.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel
import com.cuncisboss.githubuserfinal.ui.detail.DetailUserActivity
import com.cuncisboss.githubuserfinal.ui.setting.SettingActivity
import com.cuncisboss.githubuserfinal.util.Constants.EXTRA_FAV_NAME
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity(), FavoriteAdapter.FavoriteClickListener {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        adapter =
            FavoriteAdapter(this)
        rv_favorites.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        favoriteViewModel.getAllFavorites()
        favoriteViewModel.favoriteList.observe(this, Observer {
            if (it.isNotEmpty()) {
                adapter.setFavoriteList(it)
                Log.d("_log", "observeViewModel: $it")
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
            favoriteViewModel.removeFavorite(favModel.name)
            adapter.removeFavoriteList(position)
            dialog.dismiss()
        }
        builder.show()

    }
}