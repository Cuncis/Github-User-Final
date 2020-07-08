package com.cuncisboss.githubuserfinal.ui.detail.foll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.adapter.UserFollAdapter
import com.cuncisboss.githubuserfinal.ui.detail.DetailUserActivity
import com.cuncisboss.githubuserfinal.util.Utils.hideLoading
import com.cuncisboss.githubuserfinal.util.Utils.showLoading
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_follower.view.*


class FollowerFragment : Fragment(R.layout.fragment_follower) {

    lateinit var follViewModel: FollViewModel
    lateinit var userFollAdapter: UserFollAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userFollAdapter = UserFollAdapter()
        follViewModel = ViewModelProvider(requireActivity()).get(FollViewModel::class.java)
        view.rv_followers.adapter = userFollAdapter

        val act = activity as DetailUserActivity
        follViewModel.getFollowers(act.getUsername()).observe(viewLifecycleOwner, Observer { followers ->
            userFollAdapter.setFollList(followers)
        })
        follViewModel.onLoading().observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                progressBar.showLoading()
            } else {
                progressBar.hideLoading()
            }
        })
        follViewModel.getMessage().observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }

}