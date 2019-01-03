package com.example.sg772.foodorder.viewHolder

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.sg772.foodorder.Interface.itemClickListen
import com.example.sg772.foodorder.R
import kotlin.coroutines.experimental.coroutineContext


class   menuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),  View.OnClickListener{
lateinit var itemClickListen: itemClickListen
val textMenuName: TextView=itemView.findViewById(R.id.menu_name)
    val menuImage: ImageView=itemView.findViewById(R.id.menuImage)

fun setItemOnClickListener(itemClickListen: itemClickListen){
    this.itemClickListen=itemClickListen
}

    override fun onClick(v: View?) {
        itemClickListen.onClick(v!!,adapterPosition,false)
    }

}