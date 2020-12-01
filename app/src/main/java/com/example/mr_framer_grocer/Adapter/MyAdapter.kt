package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Common
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.ProductDetailsActivity
import com.example.mr_framer_grocer.R


class MyAdapter(internal var context: Context, internal var itemList: MutableList<Cart>):
        RecyclerView.Adapter<MyCartViewHolder>(){

    var mOnDataChangeListener: OnDataChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.cart_item_layout, parent, false)

        return MyCartViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        Glide.with(context).load(itemList[position].image).into(holder.cart_image)
        holder.cart_name.text = itemList[position].name
        holder.cart_price.text = context.getString(R.string.price, itemList[position].price)
        holder.qty.text = itemList[position].quantity.toString()

        val stock = itemList[position].stock
        val plusBtn = holder.plusbtn
        val minusBtn = holder.minusbtn

        if (itemList[position].quantity == 1) {
            minusBtn.isEnabled = false
            minusBtn.alpha = 0.5f
        }
        else{
            minusBtn.isEnabled = true
            minusBtn.alpha = 1.0f
        }

        if (itemList[position].quantity == stock) {
            plusBtn.isEnabled = false
            plusBtn.alpha = 0.5f
        }
        else{
            plusBtn.isEnabled = true
            plusBtn.alpha = 1.0f
        }

        plusBtn.setOnClickListener {
            // handle quantity add button
            var qty = itemList[position].quantity

            // restore the minus btn
            minusBtn.isEnabled = true
            minusBtn.alpha = 1.0f

            if (qty == stock) {
                plusBtn.isEnabled = false
                plusBtn.alpha = 0.5f
            }

            // if qty reach the number of left stock, disable and fade the plus button
            if (plusBtn.isEnabled) {
                plusBtn.alpha = 1.0f
                qty++

                if (qty == stock) {
                    plusBtn.isEnabled = false
                    plusBtn.alpha = 0.5f
                }
            }

            // update database item quantity
            itemList[position].quantity = qty
            Common.cartRepository.updateCart(itemList[position])
            // set the quantity
            holder.qty.text =  itemList[position].quantity.toString()

            if(mOnDataChangeListener != null){
                mOnDataChangeListener?.onDataChanged();
            }

        }

        minusBtn.setOnClickListener{
            // handle quantity minus button
            var qty = itemList[position].quantity

            // restore the plus
            plusBtn.isEnabled = true
            plusBtn.alpha = 1.0f

            if (qty == 1) {
                minusBtn.isEnabled = false
                minusBtn.alpha = 0.5f
            }

            // the qty cannot less then 1, disable the button when qty is 1
            if (minusBtn.isEnabled) {
                minusBtn.alpha = 1.0f
                qty--

                if (qty == 1) {
                    minusBtn.isEnabled = false
                    minusBtn.alpha = 0.5f
                }
            }

            // update database item quantity
            itemList[position].quantity = qty
            Common.cartRepository.updateCart(itemList[position])
            // set the quantity
            holder.qty.text = itemList[position].quantity.toString()

            if(mOnDataChangeListener != null){
                mOnDataChangeListener?.onDataChanged()
            }

        }

        // view product details
        holder.cart_image.setOnClickListener {
            // if any of the product is clicked, direct to product details page
            val intent = Intent(context, ProductDetailsActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("id", itemList[position].id)
            intent.putExtra("name", itemList[position].name)
            intent.putExtra("price", itemList[position].price)
            intent.putExtra("weight", itemList[position].weight)
            intent.putExtra("img", itemList[position].image)
            intent.putExtra("category", itemList[position].category)
            intent.putExtra("stock", itemList[position].stock)
            context.startActivity(intent)
        }

    }

    interface OnDataChangeListener {
        fun onDataChanged()
    }

    fun setOnDataChangeListener(onDataChangeListener: OnDataChangeListener?) {
        mOnDataChangeListener = onDataChangeListener
    }
}