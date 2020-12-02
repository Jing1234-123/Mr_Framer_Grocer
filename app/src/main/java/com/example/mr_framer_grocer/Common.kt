package com.example.mr_framer_grocer

import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.Database.favRoom.FavDatabase
import com.example.mr_framer_grocer.Database.favRoom.FavRepository

class Common {
    companion object{
        lateinit var cartDatabase: CartDatabase
        lateinit var cartRepository: CartRepository

        lateinit var favDatabase: FavDatabase
        lateinit var favRepository: FavRepository

        // user
        var contact_no: String? = null
        var psw:String? = null

    }
}