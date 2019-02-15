package com.example.sg772.foodorder.Model

import java.util.ArrayList

class Request {
    var Name: String? = null
    var Phone: String? = null
    var Street: String? = null
    var Home:String?= null
    var Flat:String?=null
    var Status: String?=null


    constructor(){

    }

    constructor(
        Name: String?,
        Phone: String?,
        Street: String?,
        Home: String?,
        Flat: String?,
        Status: String?

    ) {
        this.Name = Name
        this.Phone = Phone
        this.Street = Street
        this.Home = Home
        this.Flat = Flat
        this.Status=Status

    }


}