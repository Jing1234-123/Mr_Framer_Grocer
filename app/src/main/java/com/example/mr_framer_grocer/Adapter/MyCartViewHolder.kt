package com.example.mr_framer_grocer.Adapter


import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_framer_grocer.R

class MyCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    var cart_name: TextView
    var cart_price: TextView
    var cart_image: ImageView
    var plusbtn: Button
    var minusbtn: Button
    var qty: TextView

    init {
        cart_image = itemView.findViewById(R.id.cart_item_image)
        cart_name = itemView.findViewById(R.id.cart_item_name)
        cart_price = itemView.findViewById(R.id.cart_item_price)
        qty = itemView.findViewById(R.id.qtyrect)
        plusbtn = itemView.findViewById(R.id.plusBtn)
        minusbtn = itemView.findViewById(R.id.minusBtn)

    }
}