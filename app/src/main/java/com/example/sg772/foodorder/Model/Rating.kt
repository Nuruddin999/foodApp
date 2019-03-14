package com.example.sg772.foodorder.Model

class Rating  {

    var  ratingValue: String? = null
    var comment: String? = null
    constructor(){

    }

    constructor( ratingValue: String?,  comment: String?) {
        this.ratingValue = ratingValue
        this.comment = comment
    }


}