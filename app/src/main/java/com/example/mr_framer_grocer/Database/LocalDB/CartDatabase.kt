package com.example.mr_framer_grocer.Database.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Cart::class), version = 1)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDAO(): CartDAO

    companion object {
        @Volatile private var instance:  CartDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            CartDatabase::class.java, "MFG.db").allowMainThreadQueries()
            .build()
    }
}
