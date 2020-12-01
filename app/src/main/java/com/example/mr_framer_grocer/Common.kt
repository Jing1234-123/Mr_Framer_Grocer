package com.example.mr_framer_grocer

import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.Model.User

class Common {
    companion object{
        lateinit var cartDatabase: CartDatabase
        lateinit var cartRepository: CartRepository

        //


        // user
        var contact_no: String? = null
        var psw:String? = null

        var userInfo: User?=null

//        var totalprice = 0f
    }
}