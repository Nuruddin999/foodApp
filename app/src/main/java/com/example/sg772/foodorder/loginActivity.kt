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
import com.example.sg772.foodorder.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
            val name: EditText = findViewById(R.id.name)
            val email: EditText = findViewById(R.id.email)
            val pass: EditText = findViewById(R.id.password)
            val confpass: EditText = findViewById(R.id.conf_password)
            val reg: Button = findViewById(R.id.submit_button)
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
        val ema: EditText=findViewById(R.id.email)
        val pas: EditText=findViewById(R.id.password)
        val nam: EditText=findViewById(R.id.name)
        val user_id: String= auth.currentUser!!.uid
        val current_user_id : DatabaseReference =
            FirebaseDatabase.getInstance().getReference().child("users")


        val user=User( nam.text.toString(),ema.text.toString(),pas.text.toString())
        current_user_id.child(nam.text.toString()).setValue(user)
        Toast.makeText(this,"success",Toast.LENGTH_LONG).show()
    /* auth.createUserWithEmailAndPassword(ema.text.toString(),pas.text.toString())
         .addOnCompleteListener(this){ task ->
             if (task.isSuccessful){ val user=auth.currentUser
                 val user_id: String= auth.currentUser!!.uid
                 val current_user_id : DatabaseReference =
                     FirebaseDatabase.getInstance().getReference("users")
                 val map=HashMap<String,Any>()
                 val em:String=ema.text.toString()
                 val name:String=nam.text.toString()
                 val pa:String=pas.text.toString()
                map.put("email",em)
                 map.put("name",name)
                 map.put("password",pa)

                 val users=User(name,em,pa)
current_user_id.child(user_id).setValue(map)
                 Toast.makeText(this,"success",Toast.LENGTH_LONG).show()
             }
             else{
                 val e=task.exception
                 Toast.makeText(this,e?.message.toString(),Toast.LENGTH_LONG).show()
             }
         }*/
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
