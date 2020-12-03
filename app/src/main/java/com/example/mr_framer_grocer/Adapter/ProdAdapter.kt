package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Common
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Database.favRoom.Fav
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RelatedProdViewHolder, position: Int) {
        Glide.with(context).load(itemList[position].img).into(holder.prod_image)
        holder.prod_name.text = itemList[position].name
        holder.prod_price.text = context.getString(R.string.price, itemList[position].price)
        holder.prod_weight.text = context.getString(R.string.weight, itemList[position].weight)

        // if stock is 0, disable add to cart
        if(itemList[position].stock == 0)
        {
            holder.cart_btn.isEnabled = false
            holder.cart_btn.alpha = 0.5f
        }
        else{
            holder.cart_btn.isEnabled = true
            holder.cart_btn.alpha = 1.0f
        }

        val favItem = Common.favRepository.getFavItems()

        for(i in 0 until favItem.size)
        {
            if(itemList[position].id == favItem[i].id.toString())
                holder.favBtn.setBackgroundResource(R.drawable.prod_list_fill_heart)

        }

        // view product details
        holder.prod_image.setOnClickListener {

            // if any of the product is clicked, direct to product details page
            val intent = Intent(context, ProductDetailsActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            intent.putExtra("id", itemList[position].id)
            intent.putExtra("name", itemList[position].name)
            intent.putExtra("price", itemList[position].price)
            intent.putExtra("weight", itemList[position].weight)
            intent.putExtra("img", itemList[position].img)
            intent.putExtra("category", itemList[position].category)
            intent.putExtra("stock", itemList[position].stock)
            context.startActivity(intent)
        }

        // add to fav
        holder.favBtn.setOnClickListener {
            if(holder.favBtn.background.constantState == context.getDrawable(R.drawable.prod_list_emp_heart)!!.constantState)
            {
                holder.favBtn.setBackgroundResource(R.drawable.prod_list_fill_heart)
                val newFav = Fav(
                    itemList[position].id!!.toInt(),
                    itemList[position].name,
                    itemList[position].price,
                    itemList[position].weight,
                    itemList[position].img,
                    itemList[position].category,
                    itemList[position].stock)

                // add to database
                Common.favRepository.addToFav(newFav)
                Log.d("fav_table", Gson().toJson(newFav))
                Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show()
            }
            else
            {
                holder.favBtn.setBackgroundResource(R.drawable.prod_list_emp_heart)
                // remove from database
                Common.favRepository.delById(itemList[position].id!!.toInt())
                Toast.makeText(context, "Removed from Favorite", Toast.LENGTH_SHORT).show()
            }
        }

        // add to cart
        holder.cart_btn.setOnClickListener {
            try {
                val cartItem = Cart(
                    itemList[position].id!!.toInt(),
                    itemList[position].name,
                    itemList[position].price,
                    itemList[position].weight,
                    itemList[position].img,
                    itemList[position].category,
                    itemList[position].stock, 1
                )

                //Add to DB
                Common.cartRepository.insertToCart(cartItem)

                Log.d("MFG_debug", Gson().toJson(cartItem))

                Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show()

            } catch (ex: Exception) {
                val cartItem = Common.cartRepository.getCartItemsById(itemList[position].id!!)

                if (cartItem[0].quantity + 1 > cartItem[0].stock) {
                    Toast.makeText(context, "Quantity exceed stock level.", Toast.LENGTH_SHORT).show()
                } else {
                    val newCart = Cart(
                        itemList[position].id!!.toInt(),
                        itemList[position].name,
                        itemList[position].price,
                        itemList[position].weight,
                        itemList[position].img,
                        itemList[position].category,
                        itemList[position].stock, cartItem[0].quantity + 1
                    )

                    Common.cartRepository.updateCart(newCart)

                    Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show()
                }
            }

            if (mOnItemChangeListener != null) {
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