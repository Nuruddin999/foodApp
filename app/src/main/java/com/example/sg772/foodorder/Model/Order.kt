package com.example.sg772.foodorder.Model

import java.io.Serializable

class Order: Serializable {

    var ProductID: Int = 0
    var User: String? =null
    var ProductName: String? = null
    var Quantity: String? = null
    var Price: String? = null
    var Discount: String? = null
    constructor(){

    }

    constructor(
        User: String?,
        ProductName: String?,
        Quantity: String?,
        Price: String?,
        Discount: String?
    ) {
        this.User = User
        this.ProductName = ProductName
        this.Quantity = Quantity
        this.Price = Price
        this.Discount = Discount
    }


}