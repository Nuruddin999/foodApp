package com.example.sg772.foodorder.newVer.auth.Cart

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sg772.foodorder.newVer.auth.order.Order
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.Utils.DBHelper
import java.util.ArrayList

class CartAdapter (val list: ArrayList<Order>, val context: Context, var text: TextView, var button: Button, var listener: cartAdapterListener): RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

val v=LayoutInflater.from(p0.context).inflate(R.layout.single_cart_layout,p0,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
return  list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
val order: Order =list[p1]
        p0.productName.text=order.ProductName
        p0.productQuantity.text=order.Quantity+" "+"portions"
        p0.productPrice.text=order.Price+" "+"usd"
p0.deleteButtom.setOnClickListener {
    var removedTotal: Int=Integer.parseInt(order.Price) * Integer.parseInt(order.Quantity)
    var removed_quantity=Integer.parseInt(order.Quantity)
    Log.d("REMOVED TOTAL",removedTotal.toString())
 /*   var intent= Intent("com.example.sg772.foodorder.RemovedNumber")
    intent.putExtra("rnum",removedTotal)
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)*/
    var db= DBHelper(context)
    db.deleteData(order.ProductName.toString())
    list.remove(order)
notifyDataSetChanged()
    listener.makeChangeInText(removedTotal,removed_quantity)
    if (list.size==0){
        text.text="Cart is empty"
        button.visibility=Button.GONE
    }

}
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productName=itemView.findViewById(R.id.cart_item_name) as TextView
        val productQuantity=itemView.findViewById(R.id.cart_quantity) as TextView
        val productPrice=itemView.findViewById(R.id.cart_price) as TextView
        val deleteButtom=itemView.findViewById(R.id.delete_cart_button) as Button
    }
}