package com.example.mr_framer_grocer.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_framer_grocer.R

class MyFavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var fav_name: TextView
    var fav_price: TextView
    var fav_image: ImageView

    init {
        fav_image = itemView.findViewById(R.id.fav_item_image)
        fav_name = itemView.findViewById(R.id.fav_item_name)
        fav_price = itemView.findViewById(R.id.fav_item_price)
    }
}