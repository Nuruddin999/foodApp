package com.example.sg772.foodorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import com.example.sg772.foodorder.Model.Request
import com.example.sg772.foodorder.utils.DBHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.activity_base_nav_drawer.*
import kotlinx.android.synthetic.main.app_bar_base_nav_drawer.*
import java.util.ArrayList

open class BaseNavDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var content: FrameLayout
    lateinit var nav_menu_text: TextView
    lateinit var nav_menu_orders_in_cart_number_text: TextView
    lateinit var sign_out: LinearLayout
    lateinit var orders: LinearLayout
    lateinit var cart: LinearLayout
    lateinit var menubase: LinearLayout
    lateinit var auth: FirebaseAuth
    lateinit var requests: TextView
    lateinit var fireBaseDatabase: FirebaseDatabase
    lateinit var database_category: DatabaseReference
    lateinit var requestDatabase: DatabaseReference
    lateinit var requestList: MutableList<Request>
    lateinit var global_search: MaterialSearchBar
    lateinit var lastsuggestions: ArrayList<String>
    lateinit var ordersList: ArrayList<String>
    lateinit var userName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_nav_drawer)
        content = findViewById(R.id.baseActivityFrame)
        setSupportActionBar(toolbar)

        fireBaseDatabase = FirebaseDatabase.getInstance()
        requestDatabase = fireBaseDatabase.getReference("Requests")
        nav_menu_text = findViewById(R.id.nav_menu_text_base)
        nav_menu_orders_in_cart_number_text = findViewById(R.id.orders_in_cart_base)
        requests = findViewById(R.id.requests_base)
        sign_out = findViewById(R.id.sign_out_base)
        orders = findViewById(R.id.nav_menu_orders_base)
        cart = findViewById(R.id.cart_base)
        menubase = findViewById(R.id.menu_menu_base)
        auth = FirebaseAuth.getInstance()
        userName=findViewById(R.id.user_name_base)
        userName.text= auth.currentUser?.displayName
        loadOrdersList()

        //display on nav menu number of items in cart once it added in cart
        var mReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var numbers = intent?.getIntExtra("num", -1)
                nav_menu_orders_in_cart_number_text.text = numbers.toString()
            }

        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mReciever, object : IntentFilter("com.example.sg772.foodorder.NumOrd") {})
        //display on nav menu number of items in cart once it added in cart-end
        // go to cart
        fab.setOnClickListener { view ->
            startActivity(Intent(this@BaseNavDrawerActivity, CartActivity::class.java))
        }
        // go to cart-end
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        menubase.setOnClickListener { startActivity(Intent(this@BaseNavDrawerActivity, HomeActivity::class.java)) }
        cart.setOnClickListener { startActivity(Intent(this@BaseNavDrawerActivity, CartActivity::class.java)) }
        orders.setOnClickListener {
            startActivity(
                Intent(
                    this@BaseNavDrawerActivity,
                    RequestsListActivity::class.java
                )
            )
        }
        sign_out.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this@BaseNavDrawerActivity, MainActivity::class.java))
        }
        var db = DBHelper(this)
        var list = db.readData()
        if (list.size==0){
            nav_menu_orders_in_cart_number_text.text=""
        } else { nav_menu_orders_in_cart_number_text.text = list.size.toString()}

    }

    private fun loadOrdersList() {
        requestDatabase.orderByChild("name").equalTo(auth.currentUser?.displayName).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var count: Int = 0
                for (r in p0.children) {
                    /*var request: Request? =r.getValue(Request::class.java)
                    ordersList.add(r.key.toString())*/
                    count++
                }
if  (count>0){
    requests.text=count.toString()
} else{requests.text=""}
            }
        })

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
