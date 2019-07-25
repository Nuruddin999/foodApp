package com.example.sg772.foodorder.newVer.auth.Requests

import com.example.sg772.foodorder.Model.Order

class Request {
    var Name: String? = null
    var Phone: String? = null
    var Address: String? = null
    var Status: String?=null
   // var PaymentState: String?=null
    var Dishes: List<Order>?=null



    constructor(){

    }

    constructor(
        Name: String?,
        Phone: String?,
        Address: String?,
        Status: String?,
        //PaymentState: String?,
        Dishes: List<Order>?
    ) {
        this.Name = Name
        this.Phone = Phone
        this.Address = Address
        this.Status=Status
       // this.PaymentState = PaymentState
        this.Dishes = Dishes
    }


}