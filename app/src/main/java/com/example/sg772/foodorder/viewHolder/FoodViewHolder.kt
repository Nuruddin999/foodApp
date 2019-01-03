package com.example.sg772.foodorder.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.sg772.foodorder.Interface.itemClickListen
import com.example.sg772.foodorder.R

class   FoodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),  View.OnClickListener{
    lateinit var itemClickListen: itemClickListen
    val foodName: TextView =itemView.findViewById(R.id.food_name)
    val foodImage: ImageView =itemView.findViewById(R.id.foodImage)

    fun setItemOnClickListener(itemClickListen: itemClickListen){
        this.itemClickListen=itemClickListen
    }

    override fun onClick(v: View?) {
        itemClickListen.onClick(v!!,adapterPosition,false)
    }

}