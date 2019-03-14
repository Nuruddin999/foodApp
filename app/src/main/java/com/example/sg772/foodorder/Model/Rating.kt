package com.example.sg772.foodorder.Model

class Rating  {
    var  comment: String? = null
    var  ratingValue: String? = null

    var  user: String? = null
    constructor(){

    }

    constructor(user: String?, ratingValue: String?,  comment: String?) {
       this.user=user
        this.ratingValue = ratingValue
        this.comment = comment
    }


}