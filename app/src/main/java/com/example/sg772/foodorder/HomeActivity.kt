package com.example.sg772.foodorder

import android.content.Intent
import android.icu.util.ULocale
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import com.example.sg772.foodorder.Interface.itemClickListen
import com.example.sg772.foodorder.Model.categories
import com.example.sg772.foodorder.viewHolder.menuViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.util.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var fireBaseDatabase: FirebaseDatabase
    lateinit var database_category: DatabaseReference
    lateinit var recycler_menu: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var adapter: FirebaseRecyclerAdapter<categories, menuViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        toolbar.setTitle(getString(R.string.toolbar_title_menu))
        fireBaseDatabase = FirebaseDatabase.getInstance()
        database_category = fireBaseDatabase.getReference("categories")
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        var headerView: View = nav_view.getHeaderView(0)
        //  user_name_header.setText(commonActivity.commonUser.username)
        //Load menu
        recycler_menu = findViewById(R.id.recycler_menu)
        recycler_layoutmanager = LinearLayoutManager(this)
        recycler_menu.layoutManager = recycler_layoutmanager
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

    }


    /*  private fun loadMenu() {
    *//*    var query = FirebaseDatabase.getInstance()
            .getReference()
            .child("categories")
            .limitToLast(50)*//*

var adapter= object : FirebaseRecyclerAdapter<categories,menuViewHolder>(
    categories::class.java,
    R.layout.menu_item,
    menuViewHolder::class.java,
    database_category
){
    override fun populateViewHolder(viewHolder: menuViewHolder, model: categories, position: Int) {
        viewHolder.textMenuName.setText(model.category_name)
        Picasso.with(baseContext).load(model.image).into(viewHolder.menuImage)
        var clickItem: categories=model
        viewHolder.setItemOnClickListener(object : itemClickListen{
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
       Toast.makeText(this@HomeActivity, ""+clickItem.category_name,Toast.LENGTH_LONG).show()
            }
        })
    }
}

recycler_menu.adapter=adapter
    }*/

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
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

}
