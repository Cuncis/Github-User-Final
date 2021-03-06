package com.cuncisboss.githubuserfinal.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.adapter.UserAdapter
import com.cuncisboss.githubuserfinal.data.model.SearchUserResponse
import com.cuncisboss.githubuserfinal.data.model.UserGithub
import com.cuncisboss.githubuserfinal.data.model.UserGithubResponse
import com.cuncisboss.githubuserfinal.data.remote.ApiClient
import com.cuncisboss.githubuserfinal.repository.ApiGithubRepository
import com.cuncisboss.githubuserfinal.ui.detail.DetailUserActivity
import com.cuncisboss.githubuserfinal.ui.favorite.FavoriteActivity
import com.cuncisboss.githubuserfinal.ui.setting.SettingActivity
import com.cuncisboss.githubuserfinal.util.Constants.EXTRA_USER
import com.cuncisboss.githubuserfinal.util.Constants.MAX_SIZE
import com.cuncisboss.githubuserfinal.util.Status
import com.cuncisboss.githubuserfinal.util.Utils.hideLoadingBar
import com.cuncisboss.githubuserfinal.util.Utils.showLoadingBar
import com.cuncisboss.githubuserfinal.viewmodel.ApiGithubViewModel
import com.cuncisboss.githubuserfinal.viewmodel.ApiGithubViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search_user.*

class SearchUserActivity : AppCompatActivity(), UserAdapter.ItemClickListener {
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModelApi: ApiGithubViewModel

    private var userList = arrayListOf<UserGithub>()
    private var userTemp = arrayListOf<UserGithub>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
        userAdapter = UserAdapter(this, this)
        val repository =
            ApiGithubRepository(
                ApiClient.theGithubApi
            )
        val factory =
            ApiGithubViewModelFactory(
                repository
            )
        viewModelApi = ViewModelProvider(this, factory).get(ApiGithubViewModel::class.java)
        rv_user.adapter = userAdapter

        getUserGithub()

        observeViewModel()

        searchUser()
    }

    private fun searchUser() {
        sv_searchUser.apply {
            setIconifiedByDefault(true)
            isFocusable = false
            isIconified = false
            clearFocus()
            requestFocusFromTouch()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModelApi.getSearchUser(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.isEmpty()!!) {
                        getUserGithub()
                    }
                    return true
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModelApi.searchUser.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    layout_progressBar.hideLoadingBar(this)
                    it.data?.let { response ->
                        getUser(response)
                    }
                }
                Status.ERROR -> {
                    layout_progressBar.hideLoadingBar(this)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    layout_progressBar.showLoadingBar(this)
                }
            }
        })
    }

    private fun getUser(response: SearchUserResponse) {
        when {
            response.items.size >= MAX_SIZE -> {
                userTemp.clear()
                for (i in 0 until MAX_SIZE) {
                    val userGithub =
                        UserGithub()
                    userGithub.login = response.items[i].login
                    userGithub.avatarUrl = response.items[i].avatarUrl
                    userTemp.add(userGithub)
                }
                userAdapter.setUserList(userTemp)
            }
            response.items.size < MAX_SIZE -> {
                userTemp.clear()
                for (item in response.items) {
                    val userGithub =
                        UserGithub()
                    userGithub.login = item.login
                    userGithub.avatarUrl = item.avatarUrl
                    userTemp.add(userGithub)
                }
                userAdapter.setUserList(userTemp)
            }
        }
    }

    private fun getUserGithub() {
        userList.clear()
        val users = Gson().fromJson(getString(R.string.user_data_json), UserGithubResponse::class.java)
        userList.addAll(users.users)
        userAdapter.setUserList(userList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.github_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(userGithub: UserGithub) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(EXTRA_USER, userGithub)
        startActivity(intent)
    }
}