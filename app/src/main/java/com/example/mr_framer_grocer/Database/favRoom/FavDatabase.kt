package com.example.mr_framer_grocer.Database.favRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Fav::class), version = 2, exportSchema = false)
abstract class FavDatabase: RoomDatabase() {
    abstract fun favDao(): FavDao

    companion object{
        @Volatile
        private var INSTANCE: FavDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context)= FavDatabase.INSTANCE ?: synchronized(LOCK){
            FavDatabase.INSTANCE ?: buildDatabase(context).also { FavDatabase.INSTANCE = it}
        }

        private fun buildDatabase(context: Context) =   Room.databaseBuilder(context, FavDatabase::class.java, "dfav_table")
            .fallbackToDestructiveMigration().allowMainThreadQueries()
            .build()

//            Room.databaseBuilder(context,
//            FavDatabase::class.java, "fav_table").allowMainThreadQueries()
//            .build()


    }
}