package com.example.sg772.foodorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : loginActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val register: Button = findViewById(R.id.register)
        val login: Button = findViewById(R.id.login)
        var nam: EditText = findViewById(R.id.name_mainAct)
        var pass: EditText = findViewById(R.id.password_mainAct)


        register.setOnClickListener { startActivity(Intent(this@MainActivity, loginActivity::class.java)) }
        login.setOnClickListener {
            if (nam.text.isNullOrBlank() || pass.text.isNullOrBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()

            } else {

                logIn(nam.text.toString(), pass.text.toString())
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }

    }


    private fun logIn(email: String, password: String) {


        showprogressDialog()

        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    hideProgressDialog()
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))

                } else {
                    hideProgressDialog()
                    Log.w("fail sign in", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
}




