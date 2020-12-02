package com.example.mr_framer_grocer.Database.favRoom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavDao {
    @Query("SELECT * FROM Fav")
    fun getFavItems():List<Fav>

    @Query("SELECT * FROM Fav WHERE id= :itemId")
    fun isFav(itemId: Int): List<Fav>

    @Query("DELETE FROM Fav WHERE id= :itemId")
    fun delById(itemId: Int)

    @Insert
    fun addToFav(vararg fav: Fav)

    @Delete
    fun deleteFavItem(fav: Fav)

    @Query("DELETE FROM Fav")
    fun emptyFav()

}