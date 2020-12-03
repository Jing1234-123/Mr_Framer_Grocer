package com.example.mr_framer_grocer.Database.favRoom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Fav")
data class Fav (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "price") var price: Float?,
    @ColumnInfo(name = "weight") var weight: String?,
    @ColumnInfo(name = "image") var image: String?,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "stock") var stock: Int?
)
