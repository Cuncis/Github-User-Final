package com.cuncisboss.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.consumerapp.R
import com.cuncisboss.consumerapp.data.model.FavoriteModel
import com.cuncisboss.consumerapp.util.Constants
import com.cuncisboss.consumerapp.util.ImageHelper.Companion.getImageFromUrl
import kotlinx.android.synthetic.main.item_user_foll.view.*

class FavoriteAdapter(val favoriteClickListener: FavoriteClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var favList = ArrayList<FavoriteModel>()
        set(favList) {
            if (favList.size > 0) {
                this.favList.clear()
            }
            this.favList.addAll(favList)

            notifyDataSetChanged()
        }

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