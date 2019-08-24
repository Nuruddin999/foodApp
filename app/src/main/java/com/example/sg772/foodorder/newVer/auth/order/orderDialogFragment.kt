package com.example.sg772.foodorder.newVer.auth.order

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.sg772.foodorder.newVer.auth.Cart.successPurchase
import com.example.sg772.foodorder.newVer.auth.Requests.Request
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.Utils.Session
import com.example.sg772.foodorder.newVer.auth.Utils.paypalConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import java.math.BigDecimal
import java.util.ArrayList

class orderDialogFragment:DialogFragment() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var inflater: LayoutInflater
lateinit var orderDialogVIew:View
    lateinit var name_field:EditText
    lateinit var phone_field:EditText
    lateinit var address_field:EditText
    lateinit var total:TextView
    lateinit var buyButton:Button
    lateinit var cancelButton:Button
var username=""
    var foodname=""
    var quantity=""
    var orderslist=ArrayList<Order>()
    lateinit var orders:DatabaseReference
     var succeslistener: successPurchase?=null
    var totalam=0
    companion object {
        val PAYP_REQUEST_CODE: Int = 9999
        var paypConfig = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalConfig.clientId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = Intent(context, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypConfig)
activity?.startService(intent)
        quantity=arguments?.getString("quantity")!!
foodname= arguments?.getString("foodname")!!
     totalam= arguments?.getInt("total")!!
         var db= Session(context!!)
        var us=db.readDataSess()
            Log.d("USER",us[0].username)
            var order= Order(
                us[0].username,
                foodname,
                quantity,
                totalam.toString(),
                null
            )
            orderslist.add(order)
            username= us[0].username!!



    }
    override fun onStart() {
        super.onStart()
        var dialog:Dialog=dialog
        if (dialog!=null){
            dialog.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        alertDialog = AlertDialog.Builder(activity)
        inflater = activity?.layoutInflater!!
        orderDialogVIew = inflater?.inflate(R.layout.order_dialog, null)!!
        alertDialog.setView(orderDialogVIew)
name_field=orderDialogVIew.findViewById(R.id.place_order_name)
        phone_field=orderDialogVIew.findViewById(R.id.place_order_phone)
        address_field=orderDialogVIew.findViewById(R.id.place_order_address)
        total=orderDialogVIew.findViewById(R.id.total_order_dialog)
        buyButton=orderDialogVIew.findViewById(R.id.orderdialog_positive)
        cancelButton=orderDialogVIew.findViewById(R.id.orderdialog_negative)
        name_field.setText(username)
total.setText("$totalam  USD")
buyButton.setOnClickListener {
    if(name_field.text.isNullOrEmpty()|| phone_field.text.isNullOrEmpty() || address_field.text.isNullOrEmpty() ){
        var snackbar=Snackbar.make(orderDialogVIew,"fill empty fields",Snackbar.LENGTH_SHORT)
        snackbar.show()
        return@setOnClickListener
    }
    Log.d("click","buy")
    buy()
}
        return alertDialog.create()

    }

    private fun buy() {
        var payPalPayment = PayPalPayment(BigDecimal(totalam), "USD", "order", PayPalPayment.PAYMENT_INTENT_SALE)
        var intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypConfig)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment)
        startActivityForResult(intent, PAYP_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYP_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                var paymentConfirmation: PaymentConfirmation =
                    data!!.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (paymentConfirmation != null) {
                    try {
                        var paymentDetail: String = paymentConfirmation.toJSONObject().toString(4)
                        val request= Request(name_field.text.toString(), phone_field.text.toString(), address_field.text.toString(), "01", orderslist)
                        var mDatabase = FirebaseDatabase.getInstance().reference
                        mDatabase.child("Requests").child(System.currentTimeMillis().toString()).setValue(request).addOnCompleteListener { task ->if (task.isSuccessful)
                        {
                            Toast.makeText(context, "Thanks ! Your order is made ! ", Toast.LENGTH_LONG).show()
                        }}



                    } catch (e: JSONException) {
                        e.printStackTrace()
                        error("Something wrong")
                    }

                }
            }

        } else if (requestCode == Activity.RESULT_CANCELED)
            Toast.makeText(context, "Payment cancel ", Toast.LENGTH_LONG).show()
        else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(context, "Invalid Payment", Toast.LENGTH_LONG).show()
        // super.onActivityResult(requestCode, resultCode, data)
    }
}