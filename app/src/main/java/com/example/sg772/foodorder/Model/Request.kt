package com.example.sg772.foodorder.Model

import java.util.ArrayList

class Request {
    var Name: String? = null
    var Phone: String? = null
    var Address: String? = null
    var Status: String?=null
    var Dishes: List<Order>?=null



    constructor(){

    }

    constructor(
        Name: String?,
        Phone: String?,
        Address: String?,
        Dishes: List<Order>?

    ) {
        this.Name = Name
        this.Phone = Phone
        this.Address = Address
        this.Dishes=Dishes
        this.Status="0"

    }


}