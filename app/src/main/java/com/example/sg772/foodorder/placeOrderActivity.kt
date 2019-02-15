package com.example.sg772.foodorder

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Request
import com.example.sg772.foodorder.utils.DBHelper
import com.example.sg772.foodorder.viewHolder.RequestAdapter
import com.example.sg772.foodorder.viewHolder.placeOrderList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_place_order.*
import java.io.Serializable

/**
 * A login screen that offers login via email/password.
 */
class placeOrderActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    lateinit var Name: TextView
    lateinit var Phone: TextView
    lateinit var Street: TextView
    lateinit var Home: TextView
    lateinit var Flat: TextView
    lateinit var listOrdered: ArrayList<Order>
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var orderedFoodAdapter: RequestAdapter
    lateinit var makeOrder: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        // init
        Name = findViewById(R.id.place_order_name)
        Phone = findViewById(R.id.place_order_phone)
        Street = findViewById(R.id.place_order_street)
        Home = findViewById(R.id.place_order_house)
        Flat = findViewById(R.id.place_order_flat)
        makeOrder = findViewById(R.id.make_order_button)
        //recycler view
        var db = DBHelper(this)
        listOrdered = db.readData()
        recyclerView = findViewById(R.id.place_order_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        orderedFoodAdapter = RequestAdapter(listOrdered)
        recyclerView.adapter = orderedFoodAdapter
//place order


        makeOrder.setOnClickListener {
val request=Request(Name = Name.text.toString(),Phone = Phone.text.toString(),Street = Street.text.toString(),Home = Home.text.toString(),Flat = Flat.text.toString(),Status = "accepted")

var user: String? = FirebaseAuth.getInstance().currentUser?.displayName
            var mDatabase=FirebaseDatabase.getInstance().reference
            mDatabase.child("Requests").child(user.toString()).setValue(request)


            mDatabase.child("Requests").child(user.toString()).child("Order").setValue(listOrdered)
            startActivity(Intent(this@placeOrderActivity,RequestsListActivity::class.java))
        }


    }


    /**
     * Callback received when a permissions request has been completed.
     */


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    /**
     * Shows the progress UI and hides the login form.
     */


}
