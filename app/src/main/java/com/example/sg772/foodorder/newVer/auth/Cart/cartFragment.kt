package com.example.sg772.foodorder.newVer.auth.Cart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sg772.foodorder.newVer.auth.order.Order
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.order.orderDialogFragment
import com.example.sg772.foodorder.newVer.auth.Utils.DBHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class cartFragment : Fragment(), successPurchase, cartAdapterListener {
    override fun makeChangeInText(removed: Int, removed_quantity: Int) {
        total=total-removed
        total_quantity=total_quantity-removed_quantity
        total_money.setText("Total:  ${total} USD ${total_quantity} pieces")

    }

    override fun confirm() {
        cartList.removeAll(cartList)
        total_money.setText("")
        place_order_button.visibility=Button.GONE
    }

    lateinit var recycler_cart: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var database: FirebaseDatabase
    lateinit var request: DatabaseReference
    lateinit var total_money: TextView
    lateinit var place_order_button: Button
    lateinit var cartList: ArrayList<Order>
    lateinit var cartAdapter: CartAdapter
    var total = 0
    var total_quantity=0
    var total_name=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var View: View = inflater.inflate(R.layout.cart_fragment, container, false)
        database = FirebaseDatabase.getInstance()
        request = database.getReference("Request")
        recycler_cart = View.findViewById(R.id.recycler_cart)
        recycler_layoutmanager = LinearLayoutManager(context)
        total_money = View.findViewById(R.id.total_money)
        recycler_cart.layoutManager = recycler_layoutmanager
        place_order_button = View.findViewById(R.id.place_order)
        var db = DBHelper(context!!)
        cartList = db.readData()
        cartAdapter = CartAdapter(
            cartList,
            context!!,
            total_money,
            place_order_button,
            this
        )
        recycler_cart.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()
        if (cartList.size == 0) {
            total_money.text = "cart is empty"
            place_order_button.visibility=Button.GONE
        } else {
            place_order_button.visibility=Button.VISIBLE
            for (order: Order in cartList) {
                total += (Integer.parseInt(order.Price)) * (Integer.parseInt(order.Quantity))
                total_quantity+=Integer.parseInt(order.Quantity)
                total_name+="${order.ProductName}\n"
                Log.d("TOTALQUANTITYINCART","$total_quantity")
                total_money.text = "Total ${total.toString()}  USD  $total_quantity pieces"

            }
        }
        place_order_button.setOnClickListener {
            var bundle=Bundle()
            bundle.putInt("total",total)
            bundle.putString("foodname",total_name)
            bundle.putString("quantity",total_quantity.toString())
            Log.d("TOTAL","${total_money.toString()}, $total_quantity , $total_name ")
            var orderDialogFragment= orderDialogFragment()
            orderDialogFragment.arguments=bundle
orderDialogFragment.succeslistener=this
            orderDialogFragment.show(fragmentManager,"")


        }
        return View
    }

    override fun onResume() {
        super.onResume()
        cartAdapter.notifyDataSetChanged()

    }

}