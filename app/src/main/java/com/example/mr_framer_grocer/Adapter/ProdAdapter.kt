package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Common
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.ProductDetailsActivity
import com.example.mr_framer_grocer.R
import com.google.gson.Gson


class ProdAdapter(internal var context: Context, internal var itemList: ArrayList<Product>) :
    RecyclerView.Adapter<RelatedProdViewHolder>() {

    var mOnItemChangeListener: OnItemChangeListener? = null

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
        holder.prod_weight.text = context.getString(R.string.weight, itemList[position].weight)

        // view product details
        holder.prod_image.setOnClickListener {
            // if any of the product is clicked, direct to product details page
            val intent = Intent(context, ProductDetailsActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("id", itemList[position].id)
            intent.putExtra("name", itemList[position].name)
            intent.putExtra("price", itemList[position].price)
            intent.putExtra("weight", itemList[position].weight)
            intent.putExtra("img", itemList[position].img)
            intent.putExtra("category", itemList[position].category)
            intent.putExtra("stock", itemList[position].stock)
            context.startActivity(intent)
        }

        // add to cart
        holder.cart_btn.setOnClickListener{
            try {
                val cartItem = Cart(itemList[position].id!!.toInt(),
               itemList[position].name,
               itemList[position].price,
                itemList[position].weight,
                itemList[position].img,
                itemList[position].category,
                itemList[position].stock, 1)

                //Add to DB
                Common.cartRepository.insertToCart(cartItem)

                Log.d("MFG_debug", Gson().toJson(cartItem))

                Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show()

            } catch (ex: Exception) {
                Toast.makeText(context, "Item already added.", Toast.LENGTH_SHORT).show()
            }


            if(mOnItemChangeListener != null){
                mOnItemChangeListener?.onItemChanged()
            }

        }


    }



    interface OnItemChangeListener {
        fun onItemChanged()
    }

    fun setOnItemChangeListener(onItemChangeListener: OnItemChangeListener?) {
        mOnItemChangeListener = onItemChangeListener
    }

}