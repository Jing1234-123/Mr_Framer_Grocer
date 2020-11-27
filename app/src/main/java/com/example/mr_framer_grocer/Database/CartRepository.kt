package com.example.mr_framer_grocer.Database

import com.example.mr_framer_grocer.Database.LocalDB.Cart

class CartRepository(private var iCartDataSource: ICartDataSource) : ICartDataSource {


        companion object{
            fun getInstance(iCartDataSource: ICartDataSource): CartRepository{
                   val instance = CartRepository(iCartDataSource)
                return instance

            }
        }

    override fun getCartItems(): List<Cart> {
        return iCartDataSource.getCartItems()
    }

    override fun getCartItemsById(cartItemId: String): List<Cart> {
        return iCartDataSource.getCartItemsById(cartItemId)
    }

    override fun countCartItems(): Int {
        return iCartDataSource.countCartItems()
    }

    override fun emptyCart() {
        iCartDataSource.emptyCart()
    }

    override fun deleteCartItemById(cartItemId: String) {
        iCartDataSource.deleteCartItemById(cartItemId)
    }

    override fun insertToCart(vararg carts: Cart) {
        iCartDataSource.insertToCart(*carts)
    }

    override fun deleteCartItem(cart: Cart) {
        iCartDataSource.deleteCartItem(cart)
    }

    override fun updateCart(vararg carts: Cart) {
        iCartDataSource.updateCart(*carts)
    }
}

