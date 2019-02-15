package com.example.sg772.foodorder.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.R
import java.util.ArrayList

class placeOrderList (val list: ArrayList<Order>): RecyclerView.Adapter<placeOrderList.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): placeOrderList.ViewHolder {
    val v= LayoutInflater.from(p0.context).inflate(R.layout.single_cart_layout,p0,false)
    return ViewHolder(v)
}

override fun getItemCount(): Int {
    return  list.size
}

 override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
    val order: Order =list[p1]
    p0.productName.text=order.ProductName
    p0?.productQuantity.text=order.Quantity
    p0.productPrice.text=order.Price
}


class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val productName=itemView.findViewById(R.id.place_order_foodName) as TextView
    val productQuantity=itemView.findViewById(R.id.place_order_foodQuantity) as TextView
    val productPrice=itemView.findViewById(R.id.place_order_foodPrice) as TextView

}
}