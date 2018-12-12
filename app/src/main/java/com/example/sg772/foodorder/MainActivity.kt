package com.example.sg772.foodorder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val register: Button=findViewById(R.id.register)
        register.setOnClickListener { startActivity(Intent(this@MainActivity, loginActivity::class.java)) }
    }
}
