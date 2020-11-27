package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.ProductDetailsActivity
import com.example.mr_framer_grocer.R

class ProdAdapter(internal var context: Context, var itemList: ArrayList<Product>) :
    RecyclerView.Adapter<RelatedProdViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProdViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.product_layout, parent, false)

        return RelatedProdViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RelatedProdViewHolder, position: Int) {
        Glide.with(context).load(itemList[position].img).into(holder.prod_image)
        holder.prod_name.text = itemList[position].name
        holder.prod_price.text = context.getString(R.string.price, itemList[position].price)
        holder.prod_weight.text = context.getString(R.string.weight,itemList[position].weight)


        holder.prod_image.setOnClickListener{
            // if any of the product is clicked, direct to product details page
            val intent = Intent(context, ProductDetailsActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("id",itemList[position].id)
            intent.putExtra("name", itemList[position].name)
            intent.putExtra("price", itemList[position].price)
            intent.putExtra("weight", itemList[position].weight)
            intent.putExtra("img", itemList[position].img)
            intent.putExtra("category", itemList[position].category)
            intent.putExtra("stock", itemList[position].stock)
            context.startActivity(intent)
        }

    }

}