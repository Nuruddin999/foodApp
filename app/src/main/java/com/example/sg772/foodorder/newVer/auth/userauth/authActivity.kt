package com.example.sg772.foodorder.newVer.auth.userauth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.mainMenu.mainPage
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*
import java.util.concurrent.TimeUnit

class authActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var credential:PhoneAuthCredential?=null
    var smscode:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        auth = FirebaseAuth.getInstance()
        auth_act_log.setOnClickListener {

           var intent=Intent(this,mainPage::class.java)
            startActivity(intent)
        }


    }

    private fun sendNumber(phone: String) {

        var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                Log.d("PHONE VERIFICATION", "onVerificationCompleted:$p0")
                signInWithPhoneAuthCredentioal(p0)
                smscode=p0?.smsCode

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
