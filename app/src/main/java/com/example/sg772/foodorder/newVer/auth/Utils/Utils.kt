package com.example.sg772.foodorder.newVer.auth.Utils

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity

class Utils:AppCompatActivity() {
    var mProgressDialog: ProgressDialog? = null
    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.hide()
        }
    }

    fun showprogressDialog(activity: AppCompatActivity) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(activity)
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(false)
        }
        mProgressDialog?.show()

    }
}