package com.example.sg772.foodorder.newVer.auth.order

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.placeOrderActivity
import com.google.firebase.database.DatabaseReference
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalService

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
     totalam= arguments?.getInt("total")!!
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

        return alertDialog.create()

    }
}