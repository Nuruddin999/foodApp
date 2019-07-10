package com.example.sg772.foodorder.newVer.auth.mainMenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sg772.foodorder.FoodDetailActivity
import com.example.sg772.foodorder.Model.Food
import com.example.sg772.foodorder.Model.categories

import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.viewHolder.FoodViewHolder
import com.example.sg772.foodorder.viewHolder.menuViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [foofdListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [foofdListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class foofdListFragment : Fragment() {
    lateinit var database_category: DatabaseReference
    lateinit var recyclerViewFoodList: RecyclerView
    lateinit var fireBaseDatabase: FirebaseDatabase
    lateinit var database_food: DatabaseReference
    var categoryId: String? = null
    lateinit var recycler_layoutmanager: RecyclerView.LayoutManager
    lateinit var adapter: FirebaseRecyclerAdapter<Food, FoodViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getString("categoryID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var View: View = inflater.inflate(R.layout.fragment_foofd_list, container, false)
        fireBaseDatabase = FirebaseDatabase.getInstance()
        database_food = fireBaseDatabase.getReference("Food")
        recycler_layoutmanager = LinearLayoutManager(context)
        recyclerViewFoodList = View.findViewById(R.id.singleCategoryList)
        recyclerViewFoodList.layoutManager = recycler_layoutmanager
        if (categoryId != null) {
            loadListFood(categoryId!!)
        } else {
            loadListFood("01")
        }
        return View
    }

    private fun loadListFood(categoryId: String) {
        adapter = object : FirebaseRecyclerAdapter<Food, FoodViewHolder>(
            Food::class.java, R.layout.food_item,
            FoodViewHolder::class.java,
            database_food.orderByChild("MenuId").equalTo(categoryId.toString())
        ) {
            override fun populateViewHolder(viewHolder: FoodViewHolder?, model: Food?, position: Int) {
                viewHolder!!.foodName.setText(model!!.Name)
                Picasso.with(context).load(model.Image).into(viewHolder.foodImage)
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                    }
                })
            }
        }
        recyclerViewFoodList.adapter = adapter
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

}
