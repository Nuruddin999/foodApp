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
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RequestsListActivity : BaseNavDrawerActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayout: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_requests_list,content)
        title="Your orders"
        recyclerView = findViewById(R.id.recycler_request_list)
        linearLayout = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout

        var user = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        var dbReference =
            FirebaseDatabase.getInstance().getReference("Requests")
        var adapter = object : FirebaseRecyclerAdapter<Request, RequestListClass>(
            Request::class.java,
            R.layout.request_list_item,
            RequestListClass::class.java,
            dbReference.orderByChild("name").equalTo(user)
        ) {
            override fun populateViewHolder(viewHolder: RequestListClass?, model: Request?, position: Int) {
                viewHolder!!.reqName.setText(model!!.Name)
                viewHolder!!.reqId.setText("Id: "+getRef(position).key.toString())
                viewHolder!!.reqAddress.setText(model!!.Address)
                viewHolder!!.reqStatus.setText(converToCodeStatus(model.Status))

            }

        }
        recyclerView.adapter = adapter
    }

    private fun converToCodeStatus(status: String?): String {
        if (status.equals("0")) {
            return "Placed"
        }
        else if (status.equals("1")){
            return "On my way"
        }
        else if (status.equals("2")){
            return "Shipped"
        }
        else {
            return "Cancelled"
        }

    }
}


