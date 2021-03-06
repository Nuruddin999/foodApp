package com.example.sg772.foodorder.newVer.auth.mainMenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.AboutUsInfo.aboutUS
import com.example.sg772.foodorder.newVer.auth.Cart.cartFragment
import com.example.sg772.foodorder.newVer.auth.Requests.requestsFragment
import kotlinx.android.synthetic.main.activity_main_page.*

class mainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        var mainMenuFragment=mainMenuFragment()
        loadHomeFragment(mainMenuFragment)
main_menu_fragment.setOnClickListener {
    var mainMenuFragment=mainMenuFragment()
    loadHomeFragment(mainMenuFragment)

}
        main_menu_cart.setOnClickListener {
            Log.d("CART","CLICKED")
            var cartFragment= cartFragment()
           loadHomeFragment(cartFragment)
        }
        main_menu_request.setOnClickListener {
            var requestsFragment=requestsFragment()
            loadHomeFragment(requestsFragment)
        }
        main_menu_info.setOnClickListener {
            var aboutUS=aboutUS()
            loadHomeFragment(aboutUS)
        }
    }

    private fun loadHomeFragment(fragment: Fragment) {
        var fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_content,fragment)
        fragmentTransaction.commit()
    }

}
