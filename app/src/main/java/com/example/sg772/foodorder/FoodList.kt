package com.example.sg772.foodorder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.sg772.foodorder.Interface.itemClickListen
import com.example.sg772.foodorder.Model.Food
import com.example.sg772.foodorder.viewHolder.FoodViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_home.*

class FoodList : AppCompatActivity() {
    lateinit var fireBaseDatabase: FirebaseDatabase
    lateinit var database_food: DatabaseReference
    lateinit var recycler_food: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var categoryId: String
    lateinit var adapter: FirebaseRecyclerAdapter<Food, FoodViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        setTitle("Food")
        fireBaseDatabase = FirebaseDatabase.getInstance()
        database_food = fireBaseDatabase.getReference("Food")
        recycler_food = findViewById(R.id.recycler_food)
        recycler_layoutmanager = LinearLayoutManager(this)
        recycler_food.layoutManager = recycler_layoutmanager
        if (intent != null) {
            categoryId = intent.getStringExtra("categoryId")
        }
        if (!categoryId.isEmpty() && categoryId != null) {
            loadListFood(categoryId)
        } else {
            finish()
        }
    }

    override fun onResume() {

        categoryId = intent.getStringExtra("categoryId")
        loadListFood(categoryId)
        super.onResume()
    }

    private fun loadListFood(categoryId: String) {
        adapter = object : FirebaseRecyclerAdapter<Food, FoodViewHolder>(
            Food::class.java, R.layout.food_item,
            FoodViewHolder::class.java,
            database_food.orderByChild("MenuId").equalTo(categoryId.toString())
        ) {
            override fun populateViewHolder(viewHolder: FoodViewHolder?, model: Food?, position: Int) {
                viewHolder!!.foodName.setText(model!!.Name)
                Picasso.with(baseContext).load(model.Image).into(viewHolder.foodImage)
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val foodDeatail = Intent(this@FoodList, FoodDetailActivity::class.java)
                        foodDeatail.putExtra("FoodId", adapter.getRef(position).key)
                        startActivity(foodDeatail)
                    }
                })
            }
        }
        recycler_food.adapter = adapter
    }
}
