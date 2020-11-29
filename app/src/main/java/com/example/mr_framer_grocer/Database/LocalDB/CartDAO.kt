package com.example.mr_framer_grocer.Database.LocalDB

import androidx.room.*

@Dao
interface CartDAO {

    @Query("SELECT * FROM Cart")
    fun getCartItems(): List<Cart>

    @Query("SELECT * FROM cart WHERE id=:cartItemId")
    fun getCartItemsById(cartItemId: String): List<Cart>

    @Query("SELECT COUNT(*) FROM Cart")
    fun countCartItems(): Int

    @Query("DELETE FROM Cart")
    fun emptyCart()

    @Query("DELETE FROM Cart WHERE id=:cartItemId")
    fun deleteCartItemById(cartItemId: String)

    @Insert
    fun insertToCart(vararg carts: Cart)

    @Delete
    fun deleteCartItem(cart: Cart)

    @Update
    fun updateCart(vararg carts: Cart)
}