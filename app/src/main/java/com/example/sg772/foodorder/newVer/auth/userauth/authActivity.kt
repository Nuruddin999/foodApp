package com.example.sg772.foodorder.newVer.auth.userauth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.loginActivity
import com.example.sg772.foodorder.newVer.auth.mainMenu.mainPage
import com.example.sg772.foodorder.utils.DBHelper
import com.example.sg772.foodorder.utils.Session
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_auth.*
import java.util.concurrent.TimeUnit

class authActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var credential: PhoneAuthCredential? = null
    var smscode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        var db = DBHelper(this)
        auth = FirebaseAuth.getInstance()
        auth_email_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                auth_email_field.setBackgroundResource(R.drawable.auth_activity_fields)
            }
        })
        auth_passw_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                auth_passw_field.setBackgroundResource(R.drawable.auth_activity_fields)
            }
        })
        auth_act_log.setOnClickListener {
            if (auth_email_field.text.isNullOrEmpty() || auth_passw_field.text.isNullOrEmpty()) {
                if (auth_email_field.text.isNullOrEmpty()) {
                    auth_email_field.setBackgroundResource(R.drawable.auth_activity_empty_fields)
                }
                if (auth_passw_field.text.isNullOrEmpty()) {
                    auth_passw_field.setBackgroundResource(R.drawable.auth_activity_empty_fields)
                }
                return@setOnClickListener
            } else {

                logIn(auth_email_field.text.toString(), auth_passw_field.text.toString())
            }
        }
        auth_act_reg.setOnClickListener {
            var intent = Intent(this@authActivity, loginActivity::class.java)
            startActivity(intent)
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        auth = FirebaseAuth.getInstance()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            var intent = Intent(this@authActivity, mainPage::class.java)
//            startActivity(intent)
//        }
        var db = Session(this)
        var users = db.readDataSess()
        if (users.size > 0) {
            var intent = Intent(this@authActivity, mainPage::class.java)
            startActivity(intent)
        }

    }

    private fun logIn(email: String, password: String) {
        var users_reference = FirebaseDatabase.getInstance().reference
        users_reference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("ERROR", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    Log.d("DATACHANGE", p0.value.toString())
                    for (user in p0.children) {
                        var name = user.child("username").value
                        var paswrd = user.child("password").value
                        var phone = user.child("phone").value
                        if (name.toString().equals(email) && paswrd.toString().equals(password) || phone.toString().equals(
                                email
                            ) && paswrd.toString().equals(password)
                        ) {
                            var db = Session(this@authActivity)
                            db.insertDataSess(name.toString(), phone.toString())
                            var intent = Intent(this@authActivity, mainPage::class.java)
                            startActivity(intent)


                        }
                    }
                }
            }
        })


    }

    private fun sendNumber(phone: String) {

        var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                Log.d("PHONE VERIFICATION", "onVerificationCompleted:$p0")
                signInWithPhoneAuthCredentioal(p0)
                smscode = p0?.smsCode

            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Log.w("FAILED", "onVerificationFailed", p0)

            }


        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this, callback)

    }

    private fun signInWithPhoneAuthCredentioal(p0: PhoneAuthCredential?) {
        auth.signInWithCredential(p0!!).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("PHONE VERIFICATION", "Completed")
                val user = task?.result?.user

            }
        }
    }
}
