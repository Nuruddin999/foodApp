package com.example.sg772.foodorder.newVer.auth.order

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.sg772.foodorder.Model.Order
import com.example.sg772.foodorder.Model.Request
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.placeOrderActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalService
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
    var foodname=""
    var quantity=""
    var orderslist=ArrayList<Order>()
    lateinit var orders:DatabaseReference
    var totalam=0
    companion object {
        val PAYPAL_REQUEST_CODE: Int = 9999
        var paypalConfig = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(com.example.sg772.foodorder.utils.paypalConfig.clientId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = Intent(context, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, placeOrderActivity.paypalConfig)
activity?.startService(intent)
        quantity=arguments?.getString("quantity")!!
foodname= arguments?.getString("foodname")!!
     totalam= arguments?.getInt("total")!!
        var user=FirebaseAuth.getInstance().currentUser?.email
        Log.d("USER",user)
        var order=Order(user,foodname,quantity,totalam.toString(),null)
orderslist.add(order)
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
total.setText("$totalam  USD")
buyButton.setOnClickListener {
    if(name_field.text.isNullOrEmpty()|| phone_field.text.isNullOrEmpty() || address_field.text.isNullOrEmpty() ){
        var snackbar=Snackbar.make(orderDialogVIew,"fill empty fields",Snackbar.LENGTH_SHORT)
        snackbar.show()
        return@setOnClickListener
    }
    Log.d("click","buy")
    buy(name_field.text.toString(),phone_field.text.toString(),address_field.text.toString(),orderslist)
}
        return alertDialog.create()

    }

    private fun buy(
        name: String,
        phone: String,
        address: String,
        orderslist: ArrayList<Order>
    ) {
val request=Request(name,phone,address,"01",orderslist)
        var mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("Requests").child(System.currentTimeMillis().toString()).setValue(request)
    }
}