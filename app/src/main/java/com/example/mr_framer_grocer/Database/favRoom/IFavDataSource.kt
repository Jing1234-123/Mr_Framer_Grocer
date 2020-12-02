package com.example.mr_framer_grocer.Database.favRoom

import androidx.room.Delete
import androidx.room.Query

interface IFavDataSource {

    fun getFavItems():List<Fav>

    fun isFav(itemId: Int): List<Fav>

    fun deleteFavItem(fav: Fav)

    fun addToFav(vararg fav: Fav)

    fun delById(itemId: Int)

}