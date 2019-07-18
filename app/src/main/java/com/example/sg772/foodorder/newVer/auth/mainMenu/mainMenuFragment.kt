package com.example.sg772.foodorder.newVer.auth.mainMenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sg772.foodorder.FoodList
import com.example.sg772.foodorder.Model.categories

import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.viewHolder.menuViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [mainMenuFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [mainMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class mainMenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var database_category: DatabaseReference
lateinit var recyclerViewCategoris: RecyclerView
    lateinit var fireBaseDatabase: FirebaseDatabase

    lateinit var adapter: FirebaseRecyclerAdapter<categories, menuViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var View:View= inflater.inflate(R.layout.fragment_main_menu, container, false)
    recyclerViewCategoris=View.findViewById(R.id.mainCategoriesList)
        fireBaseDatabase = FirebaseDatabase.getInstance()
        database_category = fireBaseDatabase.getReference("categories")
        recyclerViewCategoris.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        adapter = object : FirebaseRecyclerAdapter<categories, menuViewHolder>(
            categories::class.java,
            R.layout.menu_item_new,
            menuViewHolder::class.java,
            database_category
        ) {
            override fun populateViewHolder(viewHolder: menuViewHolder?, model: categories?, position: Int) {
                if (model != null) {
                    viewHolder!!.textMenuName.setText(model.Name)
                }
                Picasso.with(context).load(model!!.Image).into(viewHolder!!.menuImage)
                val clickItem: categories = model
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
       var bundle=Bundle()
                        bundle.putString("categoryID",adapter.getRef(position).key)
                        var foofdListFragment=foofdListFragment()
                        foofdListFragment.arguments=bundle
                        var fm=fragmentManager
                        var frtrans=fm?.beginTransaction()
                        frtrans?.replace(R.id.foodlistFragmentContainer,foofdListFragment)
                        frtrans?.addToBackStack(null)
                        frtrans?.commit()
                    }
                })
            }

        }
        recyclerViewCategoris.adapter=adapter
        loadStartFragment()
        return View

    }

    private fun loadStartFragment() {
        var foofdListFragment=foofdListFragment()
        var fm=fragmentManager
        var frtrans=fm?.beginTransaction()
        frtrans?.replace(R.id.foodlistFragmentContainer,foofdListFragment)
        frtrans?.commit()
    }

    // TODO: Rename method, update argument and hook method into UI event






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
