package com.example.mr_framer_grocer

import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase

class Common {
    companion object{
        lateinit var cartDatabase: CartDatabase
        lateinit var cartRepository: CartRepository

        //
        var id: String? = null
        var name: String? = null
        var price = 0f
        var weight: String? = null
        var image:  String? = null
        var category: String? = null
        var stock = 0

//        var totalprice = 0f
    }
}