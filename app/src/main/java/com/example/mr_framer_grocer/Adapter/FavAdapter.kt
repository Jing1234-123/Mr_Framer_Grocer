package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Database.favRoom.Fav
import com.example.mr_framer_grocer.R
import com.example.mr_framer_grocer.productList

class FavAdapter(internal var context: Context, internal var itemList: MutableList<Fav>):
    RecyclerView.Adapter<MyFavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.activity_fav_layout, parent, false)

        return MyFavViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyFavViewHolder, position: Int) {
        Glide.with(context).load(itemList[position].image).into(holder.fav_image)
        holder.fav_name.text = itemList[position].name
        holder.fav_price.text = context.getString(R.string.price, itemList[position].price)

        // view item details
        holder.fav_image.setOnClickListener {
            // if any of the product is clicked, direct to product details page
            val intent = Intent(context, productList::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("id", itemList[position].id)
            intent.putExtra("name", itemList[position].name)
            intent.putExtra("price", itemList[position].price)
            intent.putExtra("weight", itemList[position].weight)
            intent.putExtra("img", itemList[position].image)
            intent.putExtra("category", itemList[position].category)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}