package com.example.sg772.foodorder

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sg772.foodorder.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class loginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var mProgressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

            var nam: EditText = findViewById(R.id.name)
            var ema: EditText = findViewById(R.id.email)
            var pho: EditText = findViewById(R.id.phone)
            var pas: EditText = findViewById(R.id.password)
            var confpass: EditText = findViewById(R.id.conf_password)
            val reg: Button = findViewById(R.id.submit_button)
            reg.setOnClickListener {
                showprogressDialog()
                if (pas.text.toString().contentEquals(confpass.text.toString())) {
                    createUser(nam.text.toString(), ema.text.toString(), pas.text.toString(), pho.text.toString())

                } else {
                    hideProgressDialog()
                    Toast.makeText(this, "no", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun writeUser(name: String, email: String, phone: String) {
       var  database=FirebaseDatabase.getInstance().reference
        val modelUser=User(name,email,null)
        database.child("users").child(name).setValue(modelUser).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
            }
            else {

                Log.d("writing", task.exception?.message)
            }

        }
        hideProgressDialog()

    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.hide()
        }
    }

    fun showprogressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this@loginActivity)
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(false)
        }
        mProgressDialog?.show()

    }

    private fun createUser(name: String, email: String, password: String, phone: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Creation User", "user created")
                    val user = auth.currentUser
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user!!.updateProfile(profileUpdate)
                    writeUser(name,email,phone)
                    hideProgressDialog()
                } else {
                    Log.d("warning", task.exception?.message)
                    Toast.makeText(
                        baseContext, "Authentication failed: "+task.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    hideProgressDialog()
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


