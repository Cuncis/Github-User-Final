package com.cuncisboss.githubuserfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.githubuserfinal.R
import com.cuncisboss.githubuserfinal.data.model.FavoriteModel
import com.cuncisboss.githubuserfinal.util.Constants
import com.cuncisboss.githubuserfinal.util.ImageHelper.Companion.getImageFromUrl
import kotlinx.android.synthetic.main.item_user_foll.view.*

class FavoriteAdapter(val favoriteClickListener: FavoriteClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var favList = arrayListOf<FavoriteModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_foll, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (favList.size >= Constants.MAX_SIZE) {
            Constants.MAX_SIZE
        } else {
            favList.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favList[position])
    }

    fun setFavoriteList(newList: List<FavoriteModel>) {
        favList.clear()
        favList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeFavoriteList(position: Int) {
        favList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favList.size)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imgProfile = view.img_profil_foll
        private val tvUsername = view.tv_name_foll

        fun bind(favModel: FavoriteModel) {
            tvUsername.text = favModel.name
            imgProfile.getImageFromUrl(favModel.image)
            itemView.setOnClickListener {
                favoriteClickListener.onItemClick(favModel, adapterPosition)
            }
        }
    }

    interface FavoriteClickListener {
        fun onItemClick(favModel: FavoriteModel, position: Int)
    }
}