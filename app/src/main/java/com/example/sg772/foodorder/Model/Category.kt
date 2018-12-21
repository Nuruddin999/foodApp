package com.example.sg772.foodorder.Model

class Category() {
    var category_name: String? = ""
    var image: String? = ""

    constructor(nm: String, im: String) : this() {
        this.category_name = nm
        this.image = im

    }
}