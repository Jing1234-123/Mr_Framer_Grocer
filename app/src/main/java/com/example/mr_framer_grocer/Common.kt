package com.example.mr_framer_grocer

import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.Model.User

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

        // user
        var contact_no: String? = null
        var psw:String? = null

        var userInfo: User?=null

//        var totalprice = 0f
    }
}