package com.example.sg772.foodorder

import android.content.Intent
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.text.style.UpdateLayout
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.login_layout.*

class loginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.title = ""
            actionBar?.setDisplayHomeAsUpEnabled(true)
            var name: EditText = findViewById(R.id.name)
            var email: EditText = findViewById(R.id.email)
            var pass: EditText = findViewById(R.id.password)
            var confpass: EditText = findViewById(R.id.conf_password)
            var reg: Button = findViewById(R.id.submit_button)
            auth = FirebaseAuth.getInstance()
            reg.setOnClickListener {
                if (pass.text.toString().contentEquals(confpass.text.toString())) {
                    createUser()
                } else {
                    Toast.makeText(this, "no", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createUser() {
     auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
         .addOnCompleteListener(this){ task ->
             if (task.isSuccessful){ val user=auth.currentUser
                 Toast.makeText(this,"success",Toast.LENGTH_LONG).show()
             }
             else{
                 val e=task.exception
                 Toast.makeText(this,e?.message,Toast.LENGTH_LONG).show()
             }
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
