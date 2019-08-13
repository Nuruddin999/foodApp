package com.example.sg772.foodorder.newVer.auth.mainMenu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.sg772.foodorder.newVer.auth.Cart.itemClickListen
import com.example.sg772.foodorder.R


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