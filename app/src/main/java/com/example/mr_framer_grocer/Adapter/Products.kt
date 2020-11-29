package com.example.mr_framer_grocer.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_framer_grocer.R

// handle related product view
class Products(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var prod_name: TextView
    var prod_price: TextView
    var prod_image: ImageView
    var prod_weight: TextView

    init {
        prod_image = itemView.findViewById(R.id.prod_img)
        prod_name = itemView.findViewById(R.id.prodName)
        prod_price = itemView.findViewById(R.id.price)
        prod_weight = itemView.findViewById(R.id.weight)
    }
}
