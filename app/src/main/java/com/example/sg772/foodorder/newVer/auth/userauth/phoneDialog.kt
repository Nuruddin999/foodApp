package com.example.sg772.foodorder.newVer.auth.userauth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.sg772.foodorder.R

class phoneDialog : DialogFragment() {
    lateinit var codeField: EditText
    lateinit var okButton: Button
    lateinit var cancelButton: Button
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var inflater: LayoutInflater

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        alertDialog = AlertDialog.Builder(activity)
        inflater = activity!!.layoutInflater
        var codeDialog: View = inflater!!.inflate(R.layout.phone_dialog, null)
        alertDialog.setView(codeDialog)

        return alertDialog.create()
    }
}