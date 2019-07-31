package com.example.sg772.foodorder

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.sg772.foodorder.newVer.auth.mainMenu.Model.Food
import com.example.sg772.foodorder.newVer.auth.mainMenu.FoodViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.mancj.materialsearchbar.MaterialSearchBar
import com.squareup.picasso.Picasso
import java.util.ArrayList

class FoodList : BaseNavDrawerActivity() {
    lateinit var database_food: DatabaseReference
    lateinit var recycler_food: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var categoryId: String
    lateinit var adapter: FirebaseRecyclerAdapter<Food, FoodViewHolder>
    lateinit var searchadapter: FirebaseRecyclerAdapter<Food, FoodViewHolder>
    lateinit var searchBar: MaterialSearchBar
    lateinit var suggestList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_food_list, content)
        // setContentView(R.layout.activity_food_list)
        if (intent != null) {
            categoryId = intent.getStringExtra("categoryId")

        }
        when (categoryId) {
            "01" -> {
                title = "Soups"
            }
            "02" -> {
                title = "Salads"
            }
            "03" -> {
                title = "Sushi"
            }
            "04" -> {
                title = "Coffee"
            }
        }
        fireBaseDatabase = FirebaseDatabase.getInstance()
        database_food = fireBaseDatabase.getReference("Food")
        recycler_food = findViewById(R.id.recycler_food)
        recycler_layoutmanager = LinearLayoutManager(this)
        recycler_food.layoutManager = recycler_layoutmanager

        if (!categoryId.isEmpty() && categoryId != null) {
            loadListFood(categoryId)
        } else {
            finish()
        }
        //search
        suggestList = ArrayList<String>()
        searchBar = findViewById(R.id.foodlist_searchbar)
        searchBar.setHint("Enter food")
        loadSuggest()
        searchBar.lastSuggestions = suggestList
        searchBar.setCardViewElevation(10)
        searchBar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var suggest = ArrayList<String>()
                for (sug in suggestList) {
                    if (sug.toLowerCase().contains(searchBar.text.toLowerCase())) {
                        suggest.add(sug)
                    }
                }
                searchBar.lastSuggestions = suggest
            }
        })
        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {

            }

            override fun onSearchStateChanged(enabled: Boolean) {
//When searchBar is closed , restore original adapter
                if (!enabled) {
                    recycler_food.adapter = adapter
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
// When search is finished , show adapter with results
                startSSearch(text)
            }
        })
    }

    private fun startSSearch(text: CharSequence?) {
        searchadapter = object : FirebaseRecyclerAdapter<Food, FoodViewHolder>
            (
            Food::class.java,
            R.layout.food_item,
            FoodViewHolder::class.java,
            database_food.orderByChild("Name").equalTo(text.toString())
        ) {
            override fun populateViewHolder(viewHolder: FoodViewHolder?, model: Food?, position: Int) {
                viewHolder!!.foodName.setText(model!!.Name)
                Picasso.with(baseContext).load(model.Image).into(viewHolder.foodImage)
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val foodDeatail = Intent(this@FoodList, FoodDetailActivity::class.java)
                        foodDeatail.putExtra("FoodId", searchadapter.getRef(position).key)
                        startActivity(foodDeatail)
                    }
                })
            }


        }
        recycler_food.adapter = searchadapter
    }

    private fun loadSuggest() {
        database_food.orderByChild("MenuId").equalTo(categoryId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (p in p0.children) {
                        var item: Food? = p.getValue(Food::class.java)
                        suggestList.add(item!!.Name.toString())
                    }
                }
            })
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
