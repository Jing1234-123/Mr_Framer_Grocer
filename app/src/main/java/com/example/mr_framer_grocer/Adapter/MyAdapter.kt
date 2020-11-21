package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Model.Item
import com.example.mr_framer_grocer.R

class MyAdapter(internal var context: Context, internal var itemList: MutableList<Item?>):
        RecyclerView.Adapter<MyCartViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.cart_item_layout, parent, false)

        val plusBtn = itemView.findViewById<Button>(R.id.plusBtn)
        val minusBtn = itemView.findViewById<Button>(R.id.minusBtn)
        val qtybox = itemView.findViewById<TextView>(R.id.qtyrect)
        val stock = 5
        plusBtn.setOnClickListener{
            // handle quantity add button
            val stringInQty = qtybox.text.toString()
            var qty = stringInQty.toInt()

            // restore the minus btn
            minusBtn.isEnabled = true
            minusBtn.alpha = 1.0f

            // if qty reach the number of left stock, disable and fade the plus button
            if (plusBtn.isEnabled) {
                plusBtn.alpha = 1.0f
                qty++

                if (qty == stock) {
                    plusBtn.isEnabled = false
                    plusBtn.alpha = 0.5f
                }
            }

            // set the quantity
            qtybox.text = qty.toString() }

       minusBtn.setOnClickListener{
           // handle quantity minus button
           val stringInQty = qtybox.text.toString()
           var qty = stringInQty.toInt()

           // restore the minus
           plusBtn.isEnabled = true
           plusBtn.alpha = 1.0f

           // the qty cannot less then 1, disable the button when qty is 1
           if (minusBtn.isEnabled) {
               minusBtn.alpha = 1.0f
               qty--

               if (qty == 1) {
                   minusBtn.isEnabled = false
                   minusBtn.alpha = 0.5f
               }
           }

           // set the quantity
           qtybox.text = qty.toString()
       }

        return MyCartViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        Glide.with(context).load(itemList[position]!!.image).into(holder.cart_image)
        holder.cart_name.text = itemList[position]!!.name
        holder.cart_price.text = itemList[position]!!.price
    }



}