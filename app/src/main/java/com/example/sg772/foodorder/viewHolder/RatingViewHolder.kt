package com.example.sg772.foodorder.viewHolder

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.view.TextureView
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.example.sg772.foodorder.Interface.itemClickListen
import com.example.sg772.foodorder.R

class RatingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var user: TextView=itemView.findViewById(R.id.username_ratinglist)
    var ratingBar: RatingBar=itemView.findViewById(R.id.ratingbar_rating_item)
    var comment: TextView=itemView.findViewById(R.id.comm_ratinglist)
}
