package com.example.mr_framer_grocer.Adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_framer_grocer.R

// handle related product view
class RelatedProdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var prod_name: TextView
    var prod_price: TextView
    var prod_image: ImageView
    var prod_weight: TextView
    var cart_btn: Button
    var favBtn: Button

    init {
        prod_image = itemView.findViewById(R.id.prod_img)
        prod_name = itemView.findViewById(R.id.prodName)
        prod_price = itemView.findViewById(R.id.price)
        prod_weight = itemView.findViewById(R.id.weight)
        cart_btn = itemView.findViewById(R.id.cartbtn)
        favBtn = itemView.findViewById(R.id.favBtn)
    }
}
