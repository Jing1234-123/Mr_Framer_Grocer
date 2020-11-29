package com.example.mr_framer_grocer.Database

import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Database.LocalDB.CartDAO

class CartDataSource(private var cartDAO: CartDAO) : ICartDataSource {

    companion object{
        fun getInstance(cartDAO: CartDAO): CartDataSource{
               val instance = CartDataSource(cartDAO)
            return instance
        }
    }

    override fun getCartItems(): List<Cart> {
        return cartDAO.getCartItems()
    }

    override fun getCartItemsById(cartItemId: String): List<Cart> {
       return cartDAO.getCartItemsById(cartItemId)
    }

    override fun countCartItems():Int {
        return cartDAO.countCartItems()
    }

    override fun emptyCart() {
        cartDAO.emptyCart()
    }

    override fun deleteCartItemById(cartItemId: String) {
        cartDAO.deleteCartItemById(cartItemId)
    }

    override fun insertToCart(vararg carts: Cart) {
       cartDAO.insertToCart(*carts)
    }

    override fun deleteCartItem(cart: Cart) {
        cartDAO.deleteCartItem(cart)
    }

    override fun updateCart(vararg carts: Cart) {
        cartDAO.updateCart(*carts)
    }


}