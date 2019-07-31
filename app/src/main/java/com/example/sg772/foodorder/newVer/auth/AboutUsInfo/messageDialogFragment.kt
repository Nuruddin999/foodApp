package com.example.sg772.foodorder.newVer.auth.AboutUsInfo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.sg772.foodorder.R

class messageDialogFragment : DialogFragment() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var inflater: LayoutInflater
    lateinit var messageDialogVIew: View
    lateinit var name_field: EditText
    lateinit var message_field: EditText
    lateinit var sendButton: Button
    lateinit var cancelButton: Button
    override fun onStart() {
        super.onStart()
        var dialog: Dialog = dialog
        if (dialog != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        alertDialog = AlertDialog.Builder(activity)
        inflater = activity?.layoutInflater!!
        messageDialogVIew = inflater?.inflate(R.layout.message_dialog, null)!!
        alertDialog.setView(messageDialogVIew)
        name_field = messageDialogVIew.findViewById(R.id.place_order_name)
        message_field = messageDialogVIew.findViewById(R.id.message_field)
        sendButton = messageDialogVIew.findViewById(R.id.messagedialog_send)
        printing()
        sendButton.setOnClickListener {
          validation()

        }
        return alertDialog.create()
    }

    private fun printing() {
        name_field.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
  name_field.background=resources.getDrawable(R.drawable.message_dialog_message_back)
                name_field.setHintTextColor(resources.getColor(android.R.color.white))

            }
        })
        message_field.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                message_field.background=resources.getDrawable(R.drawable.message_dialog_message_back)
                message_field.setHintTextColor(resources.getColor(android.R.color.white))

            }
        })
    }

    private fun validation() {
        if (name_field.text.toString().isNullOrBlank() || message_field.text.toString().isNullOrBlank()) {
            if (name_field.text.toString().isNullOrBlank()){
                name_field.background=resources.getDrawable(R.drawable.auth_activity_empty_fields)
                name_field.setHintTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
            if (message_field.text.toString().isNullOrBlank()){
                message_field.background=resources.getDrawable(R.drawable.auth_activity_empty_fields)
                message_field.setHintTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
            var snackbar= Snackbar.make(messageDialogVIew,"fill empty fields", Snackbar.LENGTH_SHORT)
            snackbar.show()
            return
        }
    }
}