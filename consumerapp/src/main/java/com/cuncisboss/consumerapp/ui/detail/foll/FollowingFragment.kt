package com.cuncisboss.consumerapp.ui.detail.foll

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.consumerapp.R
import com.cuncisboss.consumerapp.adapter.UserFollAdapter
import com.cuncisboss.consumerapp.data.remote.ApiClient
import com.cuncisboss.consumerapp.repository.ApiGithubRepository
import com.cuncisboss.consumerapp.ui.detail.DetailUserActivity
import com.cuncisboss.consumerapp.util.Status
import com.cuncisboss.consumerapp.util.Utils.hideLoading
import com.cuncisboss.consumerapp.util.Utils.showLoading
import com.cuncisboss.consumerapp.util.Utils.showView
import com.cuncisboss.consumerapp.viewmodel.ApiGithubViewModel
import com.cuncisboss.consumerapp.viewmodel.ApiGithubViewModelFactory
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_following.view.*


class FollowingFragment : Fragment(R.layout.fragment_following) {

    lateinit var follViewModel: ApiGithubViewModel
    lateinit var userFollAdapter: UserFollAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userFollAdapter = UserFollAdapter()
        val repository = ApiGithubRepository(ApiClient.theGithubApi)
        val factory = ApiGithubViewModelFactory(repository)
        follViewModel = ViewModelProvider(requireActivity(), factory).get(ApiGithubViewModel::class.java)
        view.rv_following.adapter = userFollAdapter
        val act = activity as DetailUserActivity

        observeViewModel(act.getUsername())
    }

    private fun observeViewModel(username: String) {
        follViewModel.getFollowing(username)
        follViewModel.followingList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.hideLoading()
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            userFollAdapter.setFollList(list)
                        } else {
                            tv_message.showView()
                        }
                    }
                }
                Status.ERROR -> {
                    progressBar.hideLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    progressBar.showLoading()
                }
            }
        })
    }

}