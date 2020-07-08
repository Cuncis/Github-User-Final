package com.cuncisboss.githubuserfinal.ui.detail

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.adapter.ViewPagerAdapter
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.COLUMN_IMAGE
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.COLUMN_NAME
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.IS_FAVORITE
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteHelper
import com.cuncisboss.githubuserfinal.data.local.db.MappingHelper
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel
import com.cuncisboss.githubuserfinal.data.model.UserGithub
import com.cuncisboss.githubuserfinal.ui.setting.SettingActivity
import com.cuncisboss.githubuserfinal.util.Constants
import com.cuncisboss.githubuserfinal.util.ImageHelper.Companion.getImageFromUrl
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserViewModel: DetailUserViewModel

    private var isFavorite = 0
    private lateinit var dbHelper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()

        initFavorite()

        testingString()
    }

    private fun initView() {
        dbHelper = FavoriteHelper.getInstance(this)
        dbHelper.open()

        detailUserViewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(viewPager)

        if (intent.hasExtra(Constants.EXTRA_USER)) {
            val user = intent.getParcelableExtra<UserGithub>(Constants.EXTRA_USER)!!
            if (user.username.isNotEmpty()) {
                title = user.username
                observeViewModel(user.username)
            } else {
                title = user.login
                observeViewModel(user.login)
            }
        }
    }

    private fun initFavorite() {
        fav_favorite.setOnClickListener {
            val user = intent.getParcelableExtra<UserGithub>(Constants.EXTRA_USER)!!
            if (isFavorite == 0) {
                fav_favorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_favorite
                    )
                )
                isFavorite = 1

                if (addFavorite(FavoriteModel(user.username, user.avatarUrl, 1)) > 0) {
                    Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

            } else {
                fav_favorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_favorite_border
                    )
                )
                isFavorite = 0
                if (deleteFavorite(FavoriteModel(user.username, user.avatarUrl, 0)) > 0) {
                    Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun testingString() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = dbHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                Log.d("_log", "testingString: $notes")
                Log.d("_log", "testingString: ${getUser(getUsername())}")
                Log.d("_log", "testingString: ${getUsername()}")

            } else {
                Toast.makeText(this@DetailUserActivity, "Tidak ada data saat ini", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addFavorite(favoriteModel: FavoriteModel): Long {
        val values = ContentValues()
        values.put(COLUMN_NAME, favoriteModel.name)
        values.put(COLUMN_IMAGE, favoriteModel.image)
        values.put(IS_FAVORITE, favoriteModel.isFavorite)

        return dbHelper.insert(values)
    }

    private fun deleteFavorite(favoriteModel: FavoriteModel): Int {
        return dbHelper.deleteByUsername(favoriteModel.name)
    }

    private fun getUser(username: String): Int {
        val cursor = dbHelper.queryByUsername(username)

        return if (cursor.moveToFirst()){
            cursor.getInt(3)
        } else {
            0
        }
    }

    fun getUsername(): String {
        val user = intent.getParcelableExtra<UserGithub>(Constants.EXTRA_USER)!!
        return if (user.username.isNotEmpty()) {
            user.username
        } else {
            user.login
        }
    }

    private fun observeViewModel(username: String) {
        detailUserViewModel.getDetailUser(username).observe(this, Observer { response ->
            tv_follower.text = response.followers.toString()
            tv_following.text = response.following.toString()
            img_profil.getImageFromUrl(response.avatarUrl)

            if (getUser(response.login) == 0) {
                isFavorite = 0
                fav_favorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_favorite_border
                    )
                )
            } else {
                isFavorite = 1
                fav_favorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_favorite
                    )
                )
            }
        })
        detailUserViewModel.getMessage().observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}