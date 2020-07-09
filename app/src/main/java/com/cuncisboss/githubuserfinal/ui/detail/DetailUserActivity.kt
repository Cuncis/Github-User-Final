package com.cuncisboss.githubuserfinal.ui.detail

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.adapter.ViewPagerAdapter
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.COLUMN_IMAGE
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.COLUMN_NAME
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.CONTENT_URI
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteContract.FavoriteColoums.Companion.IS_FAVORITE
import com.cuncisboss.githubuserfinal.data.local.db.FavoriteHelper
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel
import com.cuncisboss.githubuserfinal.data.model.UserGithub
import com.cuncisboss.githubuserfinal.data.remote.ApiClient
import com.cuncisboss.githubuserfinal.repository.ApiGithubRepository
import com.cuncisboss.githubuserfinal.ui.setting.SettingActivity
import com.cuncisboss.githubuserfinal.util.Constants
import com.cuncisboss.githubuserfinal.util.ImageHelper.Companion.getImageFromUrl
import com.cuncisboss.githubuserfinal.util.Status
import com.cuncisboss.githubuserfinal.viewmodel.ApiGithubViewModel
import com.cuncisboss.githubuserfinal.viewmodel.ApiGithubViewModelFactory
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserViewModel: ApiGithubViewModel

    private var isFavorite = 0
    private lateinit var dbHelper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()

        initFavorite()
    }

    private fun initView() {
        dbHelper = FavoriteHelper.getInstance(this)
        dbHelper.open()

        val repository = ApiGithubRepository(ApiClient.theGithubApi)
        val factory = ApiGithubViewModelFactory(repository)
        detailUserViewModel = ViewModelProvider(this, factory).get(ApiGithubViewModel::class.java)

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
        } else if (intent.hasExtra(Constants.EXTRA_FAV_NAME)) {
            val username = intent.getStringExtra(Constants.EXTRA_FAV_NAME)!!
            title = username
            observeViewModel(username)
        }
    }

    private fun initFavorite() {
        fav_favorite.setOnClickListener {
            if (isFavorite == 0) {
                setFavorite(false)
            } else {
                removeFavorite(false)
            }
        }
    }

    private fun setFavorite(justIcon: Boolean) {
        var username = ""
        if (intent.hasExtra(Constants.EXTRA_USER)) {
            val user = intent.getParcelableExtra<UserGithub>(Constants.EXTRA_USER)!!
            username = if (user.username.isNotEmpty()) {
                user.username
            } else {
                user.login
            }
        } else if (intent.hasExtra(Constants.EXTRA_FAV_NAME)) {
            username = intent.getStringExtra(Constants.EXTRA_FAV_NAME)!!
        }

        fav_favorite.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_baseline_favorite
            )
        )
        isFavorite = 1

        if (!justIcon) {
            detailUserViewModel.setFavorite(username)
        }
    }

    private fun removeFavorite(justIcon: Boolean) {
        var username = ""
        if (intent.hasExtra(Constants.EXTRA_USER)) {
            val user = intent.getParcelableExtra<UserGithub>(Constants.EXTRA_USER)!!
            username = if (user.username.isNotEmpty()) {
                user.username
            } else {
                user.login
            }
        } else if (intent.hasExtra(Constants.EXTRA_FAV_NAME)) {
            username = intent.getStringExtra(Constants.EXTRA_FAV_NAME)!!
        }

        fav_favorite.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_baseline_favorite_border
            )
        )
        isFavorite = 0

        if (!justIcon) {
            detailUserViewModel.setFavorite(username)
        }
    }

    private fun observeViewModel(username: String) {
        detailUserViewModel.getDetailUser(username)
        detailUserViewModel.detailUser.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        tv_follower.text = response.followers.toString()
                        tv_following.text = response.following.toString()
                        img_profil.getImageFromUrl(response.avatarUrl)

                        if (getUser(response.login) == 0) {
                            removeFavorite(true)
                        } else {
                            setFavorite(true)
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> { }
            }
        })
        detailUserViewModel.favDetail.observe(this, Observer { response ->
            if (getUser(response.login) == 0) {
                addFavorite(FavoriteModel(response.login, response.avatarUrl, 1))
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
            } else {
                deleteFavorite(FavoriteModel(response.login, response.avatarUrl, 0))
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addFavorite(favoriteModel: FavoriteModel) {
        val values = ContentValues()
        values.put(COLUMN_NAME, favoriteModel.name)
        values.put(COLUMN_IMAGE, favoriteModel.image)
        values.put(IS_FAVORITE, favoriteModel.isFavorite)

        contentResolver.insert(CONTENT_URI, values)
    }

    private fun deleteFavorite(favoriteModel: FavoriteModel) {
        val uri = Uri.parse("$CONTENT_URI/${favoriteModel.id}")
        contentResolver.delete(uri, null, null)
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
        var username = ""
        if (intent.hasExtra(Constants.EXTRA_USER)) {
            val user = intent.getParcelableExtra<UserGithub>(Constants.EXTRA_USER)!!
            username = if (user.username.isNotEmpty()) {
                user.username
            } else {
                user.login
            }
        } else if (intent.hasExtra(Constants.EXTRA_FAV_NAME)){
            username = intent.getStringExtra(Constants.EXTRA_FAV_NAME)!!
        }
        return username
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