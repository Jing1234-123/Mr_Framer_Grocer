package com.example.mr_framer_grocer.Model

class Product {

    var id: String? = null
    var name: String? = null
    var price: Float = 0f
    var weight: String? = null
    var img: String? = null
    var category: String? = null
    var stock: Int = 0

    constructor(id: String?, name: String?, price: Float, weight: String?, img: String?, category: String?, stock: Int) {

        this.id = id
        this.name = name
        this.price = price
        this.weight = weight
        this.img = img
        this.category = category
        this.stock = stock
    }

    constructor()
}