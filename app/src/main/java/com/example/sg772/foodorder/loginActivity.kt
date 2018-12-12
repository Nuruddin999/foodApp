package com.example.sg772.foodorder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem

class loginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.title = ""
            actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                val upIntent = Intent(this, MainActivity::class.java)
                NavUtils.navigateUpTo(this, upIntent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
