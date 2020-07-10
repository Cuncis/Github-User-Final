package com.cuncisboss.consumerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.consumerapp.R
import com.cuncisboss.consumerapp.data.model.UserGithub
import com.cuncisboss.consumerapp.util.ImageHelper.Companion.getImageFromDrawable
import com.cuncisboss.consumerapp.util.ImageHelper.Companion.getImageFromUrl
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.item_user_foll.view.*

class UserAdapter(val context: Context, val itemClickListener: ItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_USER = 1
        const val TYPE_ITEM = 2
    }

    private var userList = arrayListOf<UserGithub>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_USER) {
            UserViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            )
        } else {
            ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_user_foll, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_USER) {
            (holder as UserViewHolder).bind(userList[position])
        } else {
            (holder as ItemViewHolder).bind(userList[position])
        }
    }

    override fun getItemCount(): Int = userList.size

    override fun getItemViewType(position: Int): Int {
        return if (userList[position].avatar.contains("drawable")) {
            TYPE_USER
        } else {
            TYPE_ITEM
        }
    }

    fun setUserList(newUser: List<UserGithub>) {
        userList.clear()
        userList.addAll(newUser)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.tv_name
        private val tvRepo: TextView = view.tv_repo
        private val tvFollower: TextView = view.tv_follower
        private val tvFollowing: TextView = view.tv_following
        private val imgPhoto: CircleImageView = view.img_profil

        fun bind(userGithub: UserGithub) {
            tvName.text = userGithub.username
            tvRepo.text = String.format(context.getString(R.string.repo_value), userGithub.repository)
            tvFollower.text = String.format(context.getString(R.string.value_fol), userGithub.follower)
            tvFollowing.text = String.format(context.getString(R.string.value_fol), userGithub.following)
            imgPhoto.getImageFromDrawable(userGithub.avatar)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(userGithub)
            }
        }
    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.tv_name_foll
        private val imgPhoto: CircleImageView = view.img_profil_foll

        fun bind(userGithub: UserGithub) {
            tvName.text = userGithub.login
            imgPhoto.getImageFromUrl(userGithub.avatarUrl)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(userGithub)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(userGithub: UserGithub)
    }
}