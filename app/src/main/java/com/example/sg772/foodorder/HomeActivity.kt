package com.example.sg772.foodorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.util.ULocale
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.sg772.foodorder.Interface.itemClickListen
import com.example.sg772.foodorder.Model.Food
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Request
import com.example.sg772.foodorder.Model.categories
import com.example.sg772.foodorder.utils.DBHelper
import com.example.sg772.foodorder.viewHolder.FoodViewHolder
import com.example.sg772.foodorder.viewHolder.menuViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mancj.materialsearchbar.MaterialSearchBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.util.*

class HomeActivity : loginActivity(), NavigationView.OnNavigationItemSelectedListener {
   //databases
    lateinit var fireBaseDatabase: FirebaseDatabase
    lateinit var database_category: DatabaseReference
    lateinit var globalSearchSuggestionBase: DatabaseReference
    lateinit var requestDatabase: DatabaseReference
    //databases end
    lateinit var recycler_menu: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var adapter: FirebaseRecyclerAdapter<categories, menuViewHolder>
    lateinit var searchadapter: FirebaseRecyclerAdapter<Food, FoodViewHolder>
    lateinit var nav_menu_text: TextView
    lateinit var nav_menu_orders_in_cart_number_text: TextView
    lateinit var sign_out: LinearLayout
    lateinit var orders: LinearLayout
    lateinit var cart: LinearLayout
    lateinit var auth: FirebaseAuth
    lateinit var requests: TextView
    lateinit var requestList: MutableList<Request>
    lateinit var global_search: MaterialSearchBar
    lateinit var lastsuggestions: ArrayList<String>
    lateinit var userName: TextView
    lateinit var ordersList:ArrayList<String>
    lateinit var viewFlipper: ViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        title="Menu"
        fireBaseDatabase = FirebaseDatabase.getInstance()
        database_category = fireBaseDatabase.getReference("categories")
        globalSearchSuggestionBase = fireBaseDatabase.getReference("Food")
        //global search
        global_search = findViewById(R.id.global_search_home_activity)
        lastsuggestions = ArrayList<String>()
        //view Flipper

        viewFlipper=findViewById(R.id.v_flipper)
        var imageView1=ImageView(this)
        var imageView2=ImageView(this)
        var imageView3=ImageView(this)
        imageView2.scaleType=ImageView.ScaleType.FIT_XY
        imageView1.scaleType=ImageView.ScaleType.FIT_XY
        imageView3.scaleType=ImageView.ScaleType.FIT_XY
        Picasso.with(this).load("https://cdn.pixabay.com/photo/2015/03/26/09/39/cupcakes-690040__340.jpg").into(imageView1)
        Picasso.with(this).load("https://cdn.pixabay.com/photo/2014/07/08/12/34/pizza-386717__340.jpg").into(imageView2)
        Picasso.with(this).load("https://cdn.pixabay.com/photo/2013/10/16/22/34/bread-196511__340.jpg").into(imageView3)
        viewFlipper.addView(imageView1)
        viewFlipper.addView(imageView2)
        viewFlipper.addView(imageView3)
        viewFlipper.setFlipInterval(3500)


        viewFlipper.isAutoStart=true
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left)
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right)

       Log.d("slider", viewFlipper.childCount.toString())



        loadSuggest()
        global_search.lastSuggestions = lastsuggestions
        global_search.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var suggest = ArrayList<String>()
                for (sg in lastsuggestions) {
                    if (sg.toLowerCase().contains(global_search.text.toLowerCase())) {
                        suggest.add(sg)
                    }
                    global_search.lastSuggestions = suggest
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var suggest = ArrayList<String>()
                for (sg in lastsuggestions) {
                    if (sg.toLowerCase().contains(global_search.text.toLowerCase())) {
                        suggest.add(sg)
                    }
                    global_search.lastSuggestions = suggest
                }
            }
        })
        global_search.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {

            }

            override fun onSearchStateChanged(enabled: Boolean) {
                if (!enabled) {
                    recycler_menu.adapter = adapter
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSSearch(text)
            }
        })
        //global search
        fab.setOnClickListener { view ->
            startActivity(Intent(this@HomeActivity, CartActivity::class.java))
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_menu_text = findViewById(R.id.nav_menu_text)
        userName=findViewById(R.id.user_name)
        nav_menu_orders_in_cart_number_text = findViewById(R.id.orders_in_cart)
        requests = findViewById(R.id.requests)
        recycler_menu = findViewById(R.id.recycler_menu)
        sign_out = findViewById(R.id.sign_out)
        orders = findViewById(R.id.nav_menu_orders)
cart=findViewById(R.id.cart_home)
        auth= FirebaseAuth.getInstance()
        userName.text= auth.currentUser?.displayName
        nav_view.setNavigationItemSelectedListener(this )
        var mReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var numbers = intent?.getIntExtra("num", -1)
                nav_menu_orders_in_cart_number_text.text = numbers.toString()
            }

        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mReciever, object : IntentFilter("com.example.sg772.foodorder.NumOrd") {})


        //  user_name_header.setText(commonActivity.commonUser.username)
        //Load menu
       /* recycler_layoutmanager = LinearLayoutManager(this)
        recycler_menu.layoutManager = recycler_layoutmanager*/
       recycler_menu.layoutManager=GridLayoutManager(this, 2)
        adapter = object : FirebaseRecyclerAdapter<categories, menuViewHolder>(
            categories::class.java,
            R.layout.menu_item,
            menuViewHolder::class.java,
            database_category
        ) {
            override fun populateViewHolder(viewHolder: menuViewHolder?, model: categories?, position: Int) {
                if (model != null) {
                    viewHolder!!.textMenuName.setText(model.Name)
                }
                Picasso.with(baseContext).load(model!!.Image).into(viewHolder!!.menuImage)
                val clickItem: categories = model
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val food = Intent(this@HomeActivity, FoodList::class.java)
                        food.putExtra("categoryId", adapter.getRef(position).key)
                        startActivity(food)
                    }
                })
            }
        }
        recycler_menu.adapter = adapter
        var user = FirebaseAuth.getInstance().currentUser?.displayName


        //nav menu
        var mDatabase = FirebaseDatabase.getInstance().reference

/*        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (d in p0.children) {
                    var modelRequest: Request? = d.child("Requests").child(user.toString()).getValue(Request::class.java)
                    var ord: Order? =d.child("Orders").getValue(Order::class.java)
                    Log.d("loadedList", "ok")
                    requestList.add(modelRequest!!)

                }

            }
        })
        requests.text = requestList.size.toString()
*//*var requestListener=object :ValueEventListener{
    override fun onCancelled(p0: DatabaseError) {

    }

    override fun onDataChange(p0: DataSnapshot) {
       var request=p0.getValue(Request::class.java)
        var requestModel=Request(request?.Name, request?.Phone, request?.Street, request?.Home, request?.Flat,
            request?.Status
        )
        requestList.add(requestModel)
        requests.text=requestList.size.toString()
    }
}*/
        sign_out.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
        }

        orders.setOnClickListener {

            startActivity(Intent(this@HomeActivity, RequestsListActivity::class.java))
        }
        cart.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CartActivity::class.java))

        }
//display number of orders
        ordersList=ArrayList<String>()


        requestDatabase=fireBaseDatabase.getReference("Requests")
      loadOrdersList()

    }

    private fun loadOrdersList() {
        requestDatabase.orderByChild("name").equalTo(userName.text.toString()).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var count: Int=0
                for (r in p0.children){
                    /*var request: Request? =r.getValue(Request::class.java)
                    ordersList.add(r.key.toString())*/
count++
                    requests.text=count.toString()
                }
            }
        })

    }

    private fun startSSearch(text: CharSequence?) {
        searchadapter = object : FirebaseRecyclerAdapter<Food, FoodViewHolder>(
            Food::class.java,
            R.layout.food_item,
            FoodViewHolder::class.java,
            globalSearchSuggestionBase.orderByChild("Name").equalTo(text.toString())
        ) {
            override fun populateViewHolder(viewHolder: FoodViewHolder?, model: Food?, position: Int) {
                viewHolder!!.foodName.setText(model!!.Name)
                Picasso.with(baseContext).load(model.Image).into(viewHolder.foodImage)
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val foodDeatail = Intent(this@HomeActivity, FoodDetailActivity::class.java)
                        foodDeatail.putExtra("FoodId", searchadapter.getRef(position).key)
                        startActivity(foodDeatail)
                    }
                })
            }
        }
recycler_menu.adapter=searchadapter

    }


    override fun onResume() {
        var db = DBHelper(this)
        var list = db.readData()
        if (list.size==0){
            nav_menu_orders_in_cart_number_text.text=""
        } else { nav_menu_orders_in_cart_number_text.text = list.size.toString()}

        var user = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        requestList = mutableListOf()
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_menu -> {
                // Handle the camera action
            }
            R.id.nav_cart -> {

            }
            R.id.nav_orders -> {

            }
            R.id.nav_signout -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadSuggest() {
        globalSearchSuggestionBase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (p in p0.children) {
                    var item: Food? = p.getValue(Food::class.java)
                    lastsuggestions.add(item!!.Name.toString())
                }
            }
        })
    }

}

