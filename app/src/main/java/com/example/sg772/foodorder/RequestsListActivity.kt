package com.example.sg772.foodorder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.FitWindowsLinearLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Request
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RequestsListActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayout: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests_list)
        recyclerView = findViewById(R.id.recycler_request_list)
        linearLayout = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout

var user= FirebaseAuth.getInstance().currentUser?.displayName.toString()
        var dbReference =
            FirebaseDatabase.getInstance().getReference("Requests").child(user.toString()).child("Order")
        var adapter = object : FirebaseRecyclerAdapter<Order, RequestListClass>(
            Order::class.java,
            R.layout.request_list_item,
            RequestListClass::class.java,
            dbReference
        ) {
            override fun populateViewHolder(viewHolder: RequestListClass?, model: Order?, position: Int) {
                viewHolder!!.reqName.setText(model!!.ProductName)
                viewHolder!!.reqQuantity.setText(model!!.Quantity)
                viewHolder!!.reqPrice.setText(model!!.Price)
                viewHolder!!.reqStatus.setText("accepted")
        var total: Int=Integer.parseInt(model.Quantity)*Integer.parseInt(model.Price)
                viewHolder.reqTotal.text=total.toString()
            }

        }
recyclerView.adapter=adapter
    }
}


