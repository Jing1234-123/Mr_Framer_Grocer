package com.example.mr_framer_grocer.Database.LocalDB

interface ICartDataSource {
    fun getCartItems(): List<Cart>
    fun getCartItemsById(cartItemId: String): List<Cart>
    fun countCartItems(): Int
    fun emptyCart()
    fun deleteCartItemById(cartItemId: String)
    fun insertToCart(vararg todo: Cart)
    fun deleteCartItem(todo: Cart)
    fun updateCart(vararg todos: Cart)
}