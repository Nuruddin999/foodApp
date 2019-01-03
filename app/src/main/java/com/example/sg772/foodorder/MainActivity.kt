package com.example.sg772.foodorder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sg772.foodorder.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.login_layout.*

 class MainActivity : loginActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val register: Button = findViewById(R.id.register)
        val login: Button = findViewById(R.id.login)
setVisible(false)
login.setOnClickListener(this)

        /*    register.setOnClickListener { startActivity(Intent(this@MainActivity, loginActivity::class.java)) }
            .setOnClickListener { startActivity(Intent(this@MainActivity, loginActivity::class.java)) }*/

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register -> startActivity(Intent(this@MainActivity, loginActivity::class.java))
            R.id.login->logIn()

        }
    }

    private fun logIn() {
        showprogressDialog()
        val name: EditText = findViewById(R.id.name_mainAct)
        val pass: EditText = findViewById(R.id.password_mainAct)
        auth = FirebaseAuth.getInstance()
        val database:DatabaseReference=FirebaseDatabase.getInstance().getReference("users")
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                hideProgressDialog()
                Toast.makeText(this@MainActivity,p0.toException().message,Toast.LENGTH_LONG).show()

            }

            override fun onDataChange(p0: DataSnapshot) {

                val nameusr=p0.child(name.text.toString()).getValue(User::class.java)
                if (nameusr!!.password!!.equals(pass.text.toString())){
                    hideProgressDialog()
                    Toast.makeText(this@MainActivity,"ok",Toast.LENGTH_LONG).show()

                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                } else { hideProgressDialog()
                    Toast.makeText(this@MainActivity,"something wrong",Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}



