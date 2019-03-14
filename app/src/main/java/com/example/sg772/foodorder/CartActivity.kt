package com.example.sg772.foodorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.utils.DBHelper
import com.example.sg772.foodorder.utils.Database
import com.example.sg772.foodorder.viewHolder.CartAdapter

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_food_detail.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.io.Serializable
import java.lang.System.out
import java.util.ArrayList

class CartActivity : BaseNavDrawerActivity() {
    lateinit var recycler_cart: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var database: FirebaseDatabase
    lateinit var request: DatabaseReference
    lateinit var total_money:TextView
    lateinit var place_order_button: Button
    lateinit var cartList:ArrayList<Order>
    lateinit var cartAdapter: CartAdapter
     var total=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_cart,content)

fab.hide()
        //setContentView(R.layout.activity_cart)
        //Firebase
        database= FirebaseDatabase.getInstance()
        request=database.getReference("Request")
        //Init
title="Your cart"
        recycler_cart=findViewById(R.id.recycler_cart)
        recycler_layoutmanager=LinearLayoutManager(this)
total_money=findViewById(R.id.total_money)
        recycler_cart.layoutManager=recycler_layoutmanager
        place_order_button=findViewById(R.id.place_order)

       // loadListCart()


        var db=DBHelper(this)
        cartList=db.readData()
        cartAdapter=CartAdapter(cartList,this)
        recycler_cart.adapter=cartAdapter
        cartAdapter.notifyDataSetChanged()
         total=0
        if (cartList.size==0){
            total_money.text="0"
        }   else {
            for (order: Order in cartList){
                total += (Integer.parseInt(order.Price)) * (Integer.parseInt(order.Quantity))
                total_money.text = total.toString()

            }
            var mReciever= object : BroadcastReceiver(){
                override fun onReceive(context: Context?, intent: Intent?) {


                    var numbers= intent?.getIntExtra("rnum",-1)

                    var total=Integer.parseInt(total_money.text.toString())- numbers!!
                    if (total<0){
                        total_money.text="0"
                    }
                    else {
                        total_money.text = total.toString()
                    }
                }

            }
            LocalBroadcastManager.getInstance(this).registerReceiver(mReciever, object : IntentFilter("com.example.sg772.foodorder.RemovedNumber"){})


          }


        place_order_button.setOnClickListener {

           var intent=Intent(this, placeOrderActivity::class.java)
            intent.putExtra("total",Integer.parseInt(total_money.text.toString()))
startActivity(intent)
        }
    }

 /*   private fun loadListCart() {
        var db=DBHelper(this)
        cartList=db.readData()
cartAdapter=cartAdapter(cartList)
        recycler_cart.adapter=cartAdapter
        var total: Int=0
        for (order: Order in cartList){
            total+=(Integer.parseInt(order.Price))*(Integer.parseInt(order.Quantity))
            total_money.text=total.toString()
        }

    }*/
}
