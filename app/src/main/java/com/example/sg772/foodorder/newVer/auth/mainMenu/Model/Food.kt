package com.example.sg772.foodorder.newVer.auth.mainMenu.Model

class Food{
    var Description: String? = null
    var Image: String? = null
    var MenuId: String? = null
    var Name: String? = null
    var Price: Long? = null
    var averagerating=0.0
        constructor(){

        }

    constructor(Description: String?, Image: String?, MenuId: String?, Name: String?, Price: Long?) {
        this.Description = Description
        this.Image = Image
        this.MenuId = MenuId
        this.Name = Name
        this.Price = Price
    }


}