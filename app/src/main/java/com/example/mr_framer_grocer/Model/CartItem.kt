package com.example.mr_framer_grocer.Model

class CartItem {

    var name: String? = null
    var price: Float? = 0f
    var weight: String? = null
    var img: String? = null
    var category: String? = null
    var stock = 0
    var quantity = 0
    var id = 0

    constructor(name: String?, price: Float?, weight: String?, img: String?, category: String?, stock: Int, quantity: Int, id: Int) {

        this.name = name
        this.price = price
        this.weight = weight
        this.img = img
        this.category = category
        this.stock = stock
        this.quantity = quantity
        this.id = id
    }

    constructor()

}