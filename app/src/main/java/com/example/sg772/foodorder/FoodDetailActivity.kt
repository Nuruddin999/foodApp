package com.example.sg772.foodorder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.sg772.foodorder.Model.Food
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.utils.DBHelper
import com.example.sg772.foodorder.utils.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class FoodDetailActivity : AppCompatActivity() {
lateinit var food_data: DatabaseReference
    lateinit var food_name:TextView
    lateinit var food_detail_image: ImageView
    lateinit var foodID:String
    lateinit var foodPrice:TextView
    lateinit var decrButton:Button
    lateinit var incrButton:Button
    lateinit var cartButton:FloatingActionButton
    lateinit var amount:TextView
    lateinit var descr:TextView
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    lateinit var currentFood: Food
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)
    food_name=findViewById(R.id.food_name_detail)
        food_detail_image=findViewById(R.id.img_food)
        foodPrice=findViewById(R.id.food_price)
        decrButton=findViewById(R.id.decrement_button)
        incrButton=findViewById(R.id.increment_button)
        amount=findViewById(R.id.food_detail_amoumt)
        descr=findViewById(R.id.food_detail_descr)
        cartButton=findViewById(R.id.btn_cart)
        collapsingToolbarLayout=findViewById(R.id.collapsing_food_detail)
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAdapter)
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar)
        var quantity: Int=Integer.parseInt(amount.text.toString())
        val context=this
        incrButton.setOnClickListener { quantity++
        amount.text=quantity.toString()}
food_data=FirebaseDatabase.getInstance().getReference("Food")
if (intent != null){
    foodID=intent.getStringExtra("FoodId")
}
        if (foodID.isNotEmpty()){
            getFoodDetail(foodID)
        }
        cartButton.setOnClickListener {
val user= FirebaseAuth.getInstance().currentUser!!.email
            var oder=Order(user,food_name.text.toString(),amount.text.toString(),foodPrice.text.toString(),null)
            var db=DBHelper(this)
            db.insertData(oder)
            var list=db.readData()
            var intent=Intent("com.example.sg772.foodorder.NumOrd")
            intent.putExtra("num",list.size)
            LocalBroadcastManager.getInstance(this@FoodDetailActivity).sendBroadcast(intent)




            /*object : Database(this@FoodDetailActivity){}.addToCart(object : Order(foodID, currentFood.Name, amount.text.toString(), currentFood.Price.toString(), null){})
            */

            Log.d("AddToCart","ok")

        }
    }


    private fun getFoodDetail(foodID: String) {
food_data.child(foodID).addValueEventListener(object :ValueEventListener{
    override fun onCancelled(p0: DatabaseError) {
    }

    override fun onDataChange(p0: DataSnapshot) {
currentFood= p0.getValue(Food::class.java)!!
        Picasso.with(baseContext).load(currentFood?.Image).into(food_detail_image)
        collapsingToolbarLayout.title=currentFood?.Name
        foodPrice.text=currentFood?.Price.toString()
        food_name.text=currentFood?.Name
        descr.text=currentFood?.Description
    }
})
    }
}
