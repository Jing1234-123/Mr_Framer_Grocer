package com.example.mr_framer_grocer.Adapter


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_framer_grocer.R

class MyCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    var cart_name: TextView
    var cart_price: TextView
    var cart_image: ImageView
    init {
        cart_image = itemView.findViewById(R.id.cart_item_image)
        cart_name = itemView.findViewById(R.id.cart_item_name)
        cart_price = itemView.findViewById(R.id.cart_item_price)
    }
}