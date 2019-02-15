package com.example.sg772.foodorder.Model

import android.provider.ContactsContract
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

class User  {
    var username: String?=null
    var email:String?=null
    var phone:String? =null
    constructor() {

    }

    constructor(username: String?, email: String?,  phone: String?) {
        this.username = username
        this.email = email
        this.phone=phone
    }


}

