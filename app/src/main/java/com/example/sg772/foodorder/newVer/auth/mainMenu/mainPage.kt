package com.example.sg772.foodorder.newVer.auth.mainMenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sg772.foodorder.R
import kotlinx.android.synthetic.main.activity_main_page.*

class mainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        loadHomeFragment()
main_menu_fragment.setOnClickListener {




}
    }

    private fun loadHomeFragment() {
        var mainMenuFragment=mainMenuFragment()
        var fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_content,mainMenuFragment)
        fragmentTransaction.commit()
    }
}
