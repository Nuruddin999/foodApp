package com.example.sg772 .foodorder

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.example.sg772.foodorder.newVer.auth.mainMenu.Model.Rating
import com.example.sg772.foodorder.viewHolder.RatingViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference

class ratingListActivity :BaseNavDrawerActivity() {
    lateinit var ratingDatabase: DatabaseReference
    lateinit var recycler_rating: RecyclerView
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var adapter: FirebaseRecyclerAdapter<Rating, RatingViewHolder>
    lateinit var ratingBar: RatingBar
    lateinit var foodName: TextView
    lateinit var foodname:String

    lateinit var foodId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_rating_list,content)
        foodName=findViewById(R.id.foodname_ratinglist)
        ratingBar=findViewById(R.id.ratingbarfood_ratingList)
        recycler_rating=findViewById(R.id.ratingLIstview)
//extras
        foodId=intent.getStringExtra("foodid")
Log.d("food",foodId)
        foodname=intent.getStringExtra("foodName")
        var rating: Float=intent.getFloatExtra("rating",0F)
//
        foodName.text=foodname
        ratingBar.rating=rating
        recycler_layoutmanager = LinearLayoutManager(this)
        recycler_rating.layoutManager=recycler_layoutmanager
        ratingDatabase=fireBaseDatabase.getReference("Rating")
        adapter = object : FirebaseRecyclerAdapter<Rating, RatingViewHolder>(
            Rating::class.java,
            R.layout.rating_list_item,
            RatingViewHolder::class.java,
            ratingDatabase.child(foodId)
            ){
            override fun populateViewHolder(viewHolder: RatingViewHolder?, model: Rating?, position: Int) {
viewHolder!!.user.setText(model!!.user.toString())
                viewHolder.ratingBar.rating= model.ratingValue!!.toFloat()
                viewHolder.comment.text=model.comment
            }
        }
recycler_rating.adapter=adapter
    }
}
