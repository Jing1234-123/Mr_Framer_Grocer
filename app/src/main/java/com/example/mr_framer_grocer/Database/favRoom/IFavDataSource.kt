package com.example.mr_framer_grocer.Database.favRoom

interface IFavDataSource {

    fun getFavItems():List<Fav>

    fun isFav(itemId: Int): List<Fav>

    fun deleteFavItem(fav: Fav)

    fun addToFav(vararg fav: Fav)

    fun delById(itemId: Int)

    fun emptyFav()
}