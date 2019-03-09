package com.example.sg772.foodorder

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Request
import com.example.sg772.foodorder.utils.DBHelper
import com.example.sg772.foodorder.viewHolder.RequestAdapter
import com.example.sg772.foodorder.viewHolder.placeOrderList
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.paypal.android.sdk.payments.*

import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.app_bar_base_nav_drawer.*
import org.json.JSONException
import java.io.Serializable
import java.math.BigDecimal

/**
 * A login screen that offers login via email/password.
 */
class placeOrderActivity : BaseNavDrawerActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    lateinit var Name: TextView
    lateinit var Phone: TextView
    lateinit var Address: TextView
    lateinit var listOrdered: ArrayList<Order>
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var orderedFoodAdapter: RequestAdapter
    lateinit var total: TextView
    lateinit var makeOrder: Button

    companion object {
        val PAYPAL_REQUEST_CODE: Int = 9999
        var paypalConfig = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(com.example.sg772.foodorder.utils.paypalConfig.clientId)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_place_order, content)
        fab.hide()
        title = "Checkout"
        // setContentView(R.layout.activity_place_order)
        // init
        //paypal
        var intent = Intent(this, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        startService(intent)
        //
        total = findViewById(R.id.total_money_plae_order)
        Name = findViewById(R.id.place_order_name)
        Phone = findViewById(R.id.place_order_phone)
        Address = findViewById(R.id.place_order_address)
        makeOrder = findViewById(R.id.make_order_button)
        //recycler view
        var db = DBHelper(this)
        listOrdered = db.readData()
        var intentFromCart=getIntent()
        var totalPrice=intentFromCart.getIntExtra("total",0)

        total.text = totalPrice.toString()
        recyclerView = findViewById(R.id.place_order_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        orderedFoodAdapter = RequestAdapter(listOrdered)
        recyclerView.adapter = orderedFoodAdapter
        var user: String? = FirebaseAuth.getInstance().currentUser?.displayName
        Name.text = user
//place order


        makeOrder.setOnClickListener {

            var payPalPayment = PayPalPayment(BigDecimal(totalPrice), "USD", "order", PayPalPayment.PAYMENT_INTENT_SALE)
            var intent = Intent(applicationContext, PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment)
            startActivityForResult(intent, PAYPAL_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                var paymentConfirmation: PaymentConfirmation =
                    data!!.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (paymentConfirmation != null) {
                    try {
                        var paymentDetail: String = paymentConfirmation.toJSONObject().toString(4)

                        val request = Request(
                            Name = Name.text.toString(),
                            Phone = Phone.text.toString(),
                            Address = Address.text.toString(),
                            Status = "Accepted",
                            Dishes = listOrdered
                        )
                        var mDatabase = FirebaseDatabase.getInstance().reference
                        mDatabase.child("Requests").child(System.currentTimeMillis().toString()).setValue(request)
                        object : DBHelper(baseContext) {}.deleteAll()
                        Toast.makeText(this, "Thanks ! Your order is made ! ", Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        error("Something wrong")
                    }

                }
            }

        } else if (requestCode == Activity.RESULT_CANCELED)
            Toast.makeText(this, "Payment cancel ", Toast.LENGTH_LONG).show()
        else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid Payment", Toast.LENGTH_LONG).show()
        // super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * Callback received when a permissions request has been completed.
     */


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    /**
     * Shows the progress UI and hides the login form.
     */


}
