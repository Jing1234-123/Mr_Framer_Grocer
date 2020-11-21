package com.example.mr_framer_grocer.Model

class Product {

    var name: String? = null
    var price: String? = null
    var weight: String? = null
    var img: String? = null

    constructor(name: String?, price: String?, weight: String?, img: String?) {
        this.name = name
        this.price = price
        this.weight = weight
        this.img = img
    }
}