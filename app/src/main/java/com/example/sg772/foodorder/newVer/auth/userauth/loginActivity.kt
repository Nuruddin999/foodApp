package com.example.sg772.foodorder.newVer.auth.userauth

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sg772.foodorder.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login_layout.*

open class loginActivity : AppCompatActivity() {
    private var mProgressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

            var nam: EditText = findViewById(R.id.name)
            var pho: EditText = findViewById(R.id.phone)
            var pas: EditText = findViewById(R.id.password)
            var confpass: EditText = findViewById(R.id.conf_pasword)
            val reg: Button = findViewById(R.id.submit_button)
            reg.setOnClickListener {
                validation(nam.text.toString(),phone.text.toString(), pas.text.toString(), confpass.text.toString() )
                showprogressDialog()
                if (pas.text.toString().contentEquals(confpass.text.toString())) {
writeUser(nam.text.toString(),pho.text.toString(),pas.text.toString())
                } else {
                    hideProgressDialog()
                    Toast.makeText(this, "no", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun validation(name: String, phone: String, pass: String, confpass: String) {
if (name.isNullOrEmpty()|| phone.isNullOrEmpty() || pass.isNullOrEmpty() || confpass.isNullOrEmpty() ){
    var layt: View =findViewById(R.id.login_view)
    Snackbar.make(
        layt,
        "Fill empty fields",
        Snackbar.LENGTH_SHORT
    ).show()
    return
}
    }

    private fun writeUser(name: String,  phone: String,pass:String) {
       var  database=FirebaseDatabase.getInstance().reference
        val modelUser= User(name, null, phone, pass)
        database.child("users").child(name).setValue(modelUser).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
                var intent = Intent(this@loginActivity, authActivity::class.java)
                startActivity(intent)
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

/*
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
*/

}


