package com.example.sg772.foodorder.Interface

import android.view.View

interface itemClickListen {
    fun onClick(view: View, position: Int, isLongClick: Boolean)
}