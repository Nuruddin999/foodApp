package com.example.sg772.foodorder.newVer.auth.mainMenu

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sg772.foodorder.Model.Food
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Rating
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.order.orderDialogFragment
import com.example.sg772.foodorder.utils.DBHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_food.*

class singleFoodFragment : Fragment() {
    lateinit var ratingReference: DatabaseReference
    lateinit var food_name: TextView
    lateinit var food_detail_image: ImageView
    lateinit var foodID: String
    lateinit var addfavorite: ImageView
    lateinit var foodPrice: TextView
    lateinit var decrButton: ImageView
    lateinit var incrButton: ImageView
    lateinit var cartButton: ImageView
    lateinit var rating: RatingBar
    lateinit var descr: TextView
    lateinit var amount: TextView
    lateinit var makeOrder: Button
    lateinit var food_data: DatabaseReference
    lateinit var currentFood: Food
    lateinit var auth: FirebaseAuth
    lateinit var add_rating:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodID = arguments!!.getString("foodID")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = layoutInflater.inflate(R.layout.single_food, container, false)
        auth = FirebaseAuth.getInstance()
        initview(view)
        var quantity: Int = Integer.parseInt(amount.text.toString())
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
        ratingReference=FirebaseDatabase.getInstance().getReference("Rating")

        getFoodDetail(foodID)
       getFoodRating(foodID)
        add_rating.setOnClickListener {
            showRatingDialog(rating)
        }
makeOrder.setOnClickListener {
    var totalAmout=Integer.parseInt(foodPrice.text.toString())*Integer.parseInt(amount.text.toString())

    var bundle=Bundle()
    bundle.putInt("total",totalAmout)
    bundle.putString("foodname",food_name.text.toString())
    bundle.putString("quantity",amount.text.toString())
    Log.d("TOTAL","${totalAmout.toString()}, ${amount.text.toString()} , ${food_name.text.toString()} ")
    var orderDialogFragment=orderDialogFragment()
    orderDialogFragment.arguments=bundle

    orderDialogFragment.show(fragmentManager,"")

}
        cartButton.setOnClickListener {
            Log.d("cart", "CLICKED")

            if (amount.text.contains("0")) {
                Toast.makeText(activity, "Please choose quantity", Toast.LENGTH_LONG).show()
            } else {
                val user = FirebaseAuth.getInstance().currentUser!!.email
                var oder =
                    Order(user, food_name.text.toString(), amount.text.toString(), foodPrice.text.toString(), null)
                var db = DBHelper(context!!)
                db.insertData(oder)
                var list = db.readData()



                /*object : Database(this@FoodDetailActivity){}.addToCart(object : Order(foodID, currentFood.Name, amount.text.toString(), currentFood.Price.toString(), null){})
                */

                Log.d("AddToCart", "ok")
            }
        }
        return view

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
                        food_data.child(foodID).child("averagerating").setValue(average)
                        rating.rating = average.toFloat()
                        Log.d("average", average.toString())
                    }
                }
            })
    }

    private fun initview(view: View) {
        food_name = view!!.findViewById(R.id.food_name_detail_single)
        descr = view!!.findViewById(R.id.food_descr_detail_single)
        food_detail_image = view!!.findViewById(R.id.img_food)
        foodPrice = view!!.findViewById(R.id.food_price)
        decrButton = view!!.findViewById(R.id.decrement_button)
        amount = view!!.findViewById(R.id.food_detail_amoumt)
        incrButton = view!!.findViewById(R.id.increment_button)
        rating = view!!.findViewById(R.id.ratingbar_single)
        cartButton = view!!.findViewById(R.id.add_tocart_button)
        makeOrder = view!!.findViewById(R.id.make_order_button)
        addfavorite = view!!.findViewById(R.id.add_tofavorite_button)
        add_rating=view!!.findViewById(R.id.add_rating_button)
    }

    private fun getFoodDetail(foodID: String) {
        food_data.child(foodID).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                currentFood = p0.getValue(Food::class.java)!!
                Picasso.with(context).load(currentFood?.Image).into(food_detail_image)
                foodPrice.text = currentFood?.Price.toString()
                food_name.text = currentFood?.Name
                descr.text = currentFood?.Description
            }
        })
    }

    private fun showRatingDialog(rating: RatingBar) {
        val ratingDialog = AlertDialog.Builder(context)
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

            var rating = Rating(auth.currentUser?.displayName.toString(), RatingValue.toString(), Comments)
            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
Log.d("ERROR",p0.message)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.child(foodID).child(auth.currentUser?.displayName.toString()).exists()) {
                            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString()).removeValue()
                            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString())
                                .setValue(rating)
                          getFoodRating(foodID)


                        } else {
                            ratingReference.child(foodID).child(auth.currentUser?.displayName.toString())
                                .setValue(rating)
                            getFoodRating(foodID)

                        }

                    }
                })

        }

        ratingDialogBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> }

    }

}