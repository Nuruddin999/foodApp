package com.example.sg772.foodorder.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Request
import com.example.sg772.foodorder.R
import java.util.ArrayList

class RequestAdapter(val list: ArrayList<Order>):RecyclerView.Adapter<RequestAdapter.Viewholder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Viewholder {
        val v= LayoutInflater.from(p0.context).inflate(R.layout.place_order_listfood,p0,false)
        return Viewholder(v)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(p0: Viewholder, p1: Int) {
        val requestOrder: Order =list[p1]
        p0.reqName.text=requestOrder.ProductName
        p0.reqQuantity.text=requestOrder.Quantity
        p0.reqPrice.text=requestOrder.Price
    }

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val reqName=itemView.findViewById(R.id.place_order_foodName) as TextView
        val reqQuantity=itemView.findViewById(R.id.place_order_foodQuantity) as TextView
        val reqPrice=itemView.findViewById(R.id.place_order_foodPrice) as TextView
    }
}