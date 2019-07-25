package com.example.sg772.foodorder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.*
import com.example.sg772.foodorder.newVer.auth.mainMenu.Model.Food
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.newVer.auth.mainMenu.Model.Rating
import com.example.sg772.foodorder.utils.DBHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_home.*

class FoodDetailActivity : BaseNavDrawerActivity() {
    lateinit var food_data: DatabaseReference
    lateinit var ratingReference: DatabaseReference
    lateinit var food_name: TextView
    lateinit var food_detail_image: ImageView
    lateinit var foodID: String
    lateinit var foodPrice: TextView
    lateinit var decrButton: Button
    lateinit var incrButton: Button
    lateinit var cartButton: FloatingActionButton
    lateinit var ratingButton: FloatingActionButton
    lateinit var amount: TextView
    lateinit var descr: TextView
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    lateinit var currentFood: Food
    lateinit var ratingBar: RatingBar
    lateinit var cancelDialog: Button
    lateinit var seeAllFeedback: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_food_detail, content)
        fab.hide()
        // setContentView(R.layout.activity_food_detail)
        food_name = findViewById(R.id.food_name_detail)
        food_detail_image = findViewById(R.id.img_food)
        foodPrice = findViewById(R.id.food_price)
        decrButton = findViewById(R.id.decrement_button)
        incrButton = findViewById(R.id.increment_button)
        amount = findViewById(R.id.food_detail_amoumt)
        descr = findViewById(R.id.food_detail_descr)
        cartButton = findViewById(R.id.btn_cart)
        ratingButton = findViewById(R.id.btn_rating)
        ratingBar = findViewById(R.id.ratingbar)
        seeAllFeedback = findViewById(R.id.sellallfeedbackbutton)
        collapsingToolbarLayout = findViewById(R.id.collapsing_food_detail)
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAdapter)
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar)
        var quantity: Int = Integer.parseInt(amount.text.toString())
        val context = this
        ratingReference = fireBaseDatabase.getReference("Rating")
        decrButton.setOnClickListener {
            if (amount.text.equals("0")) {
                amount.text = "0"
            } else {
                quantity--
                amount.text = quantity.toString()
            }
        }
        incrButton.setOnClickListener {
            quantity++
            amount.text = quantity.toString()
        }
        food_data = FirebaseDatabase.getInstance().getReference("Food")
        if (intent != null) {
            foodID = intent.getStringExtra("FoodId")
        }
        if (foodID.isNotEmpty()) {
            getFoodDetail(foodID)
            getFoodRating(foodID)
        }
        cartButton.setOnClickListener {
            if (amount.text.contains("0")) {
                Toast.makeText(this, "Please choose quantity", Toast.LENGTH_LONG).show()
            } else {
                val user = FirebaseAuth.getInstance().currentUser!!.email
                var oder =
                    Order(user, food_name.text.toString(), amount.text.toString(), foodPrice.text.toString(), null)
                var db = DBHelper(this)
                db.insertData(oder)
                var list = db.readData()
                var intent = Intent("com.example.sg772.foodorder.NumOrd")
                intent.putExtra("num", list.size)
                LocalBroadcastManager.getInstance(this@FoodDetailActivity).sendBroadcast(intent)


                /*object : Database(this@FoodDetailActivity){}.addToCart(object : Order(foodID, currentFood.Name, amount.text.toString(), currentFood.Price.toString(), null){})
                */

                Log.d("AddToCart", "ok")
            }

        }
        //rating
        ratingButton.setOnClickListener {
            showRatingDialog()
        }

        seeAllFeedback.setOnClickListener {
            val ratingList = Intent(this@FoodDetailActivity, ratingListActivity::class.java)
            ratingList.putExtra("foodid", foodID)
            ratingList.putExtra("foodName", food_name.text.toString())
            ratingList.putExtra("rating", ratingBar.rating)
            startActivity(ratingList)
        }
    }

    private fun getFoodRating(IDfood: String) {
        ratingReference.child(IDfood).orderByChild(auth.currentUser?.displayName.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                var sum: Double = 0.0
                var count: Int = 0
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("success", p0.value.toString())
                    for (l in p0.children) {
                        var rateValue: String = l.child("ratingValue").getValue(String::class.java)!!
                        sum += rateValue.toDouble()
                        count++
                        Log.d("value", sum.toString() + " " + count)
                    }
                    if (count > 0) {
                        var average = sum / count
                        ratingBar.rating = average.toFloat()
                        Log.d("average", average.toString())
                    }
                }
            })
    }


    private fun showRatingDialog() {
        val ratingDialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.rating_dialog_layout, null)
        var ratingDialogBar: RatingBar = dialogView.findViewById(R.id.ratingDialogBar)
        var ratingFeedback: EditText = dialogView.findViewById(R.id.ratingFeedback)
        ratingDialog.setView(dialogView)

        ratingDialog.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        ratingDialog.setPositiveButton("Submit", { dialogInterface: DialogInterface, i: Int -> })
        ratingDialog.setTitle("Rate this food")

        val customDialog = ratingDialog.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var RatingValue = ratingDialogBar.rating
            var Comments = ratingFeedback.text.toString()

            var rating = Rating(
                auth.currentUser?.displayName.toString(),
                RatingValue.toString(),
                Comments
            )
            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.d("error", p0.message)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.child(foodID).child(auth.currentUser?.displayName.toString()).exists()) {
                            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString()).removeValue()
                            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString())
                                .setValue(rating)
                        } else {
                            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString())
                                .setValue(rating)

                        }

                    }
                })
            finish()
        }

        ratingDialogBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> }

    }


    private fun getFoodDetail(foodID: String) {
        food_data.child(foodID).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                currentFood = p0.getValue(Food::class.java)!!
                Picasso.with(baseContext).load(currentFood?.Image).into(food_detail_image)
                foodPrice.text = currentFood?.Price.toString()
                food_name.text = currentFood?.Name
                descr.text = currentFood?.Description
            }
        })
    }
}
