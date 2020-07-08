package com.cuncisboss.githubuserfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel
import com.cuncisboss.githubuserfinal.data.model.FollModel
import com.cuncisboss.githubuserfinal.util.Constants.MAX_SIZE
import com.cuncisboss.githubuserfinal.util.ImageHelper.Companion.getImageFromUrl
import kotlinx.android.synthetic.main.item_user_foll.view.*

class UserFollAdapter : RecyclerView.Adapter<UserFollAdapter.ViewHolder>() {

    private var follList = arrayListOf<FollModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_foll, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (follList.size >= MAX_SIZE) {
            MAX_SIZE
        } else {
            follList.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(follList[position])
    }

    fun setFollList(newList: List<FollModel>) {
        follList.clear()
        follList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imgProfile = view.img_profil_foll
        private val tvUsername = view.tv_name_foll

        fun bind(follModel: FollModel) {
            tvUsername.text = follModel.login
            imgProfile.getImageFromUrl(follModel.avatarUrl)
        }
    }
}