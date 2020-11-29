package com.example.mr_framer_grocer.Database.LocalDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cart")

data class Cart(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "price") var price: Float?,
    @ColumnInfo(name = "weight") var weight: String?,
    @ColumnInfo(name = "image") var image: String?,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "stock") var stock: Int,
    @ColumnInfo(name = "quantity") var quantity: Int



)
//    @Ignore
//    constructor(name: String?, price: Float, weight: String?, image: String?, category: String?, stock: Int, quantity: Int)
//    {
//        this.name = name
//        this.price = price
//        this.weight = weight
//        this.image = image
//        this.category = category
//        this.stock = stock
//    }


