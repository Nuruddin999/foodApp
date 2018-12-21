package com.example.sg772.foodorder.Model

import android.provider.ContactsContract
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

class User () {
    var username: String?=""
    var email:String?=""
    var password:String? =""
    constructor(  us:String,  em:String,  pass:String): this (){
        this.username=us
        this.email=em
        this.password=pass
    }
}

