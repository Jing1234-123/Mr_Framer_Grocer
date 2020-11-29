package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Common
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.R


class MyAdapter(internal var context: Context, internal var itemList: MutableList<Cart>):
        RecyclerView.Adapter<MyCartViewHolder>(){


    lateinit var plusBtn: Button
    lateinit var minusBtn: Button
    lateinit var qtybox: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.cart_item_layout, parent, false)

        plusBtn = itemView.findViewById(R.id.plusBtn)
        minusBtn = itemView.findViewById(R.id.minusBtn)
        qtybox = itemView.findViewById(R.id.qtyrect)

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

        plusBtn.setOnClickListener {
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

            // update database item quantity
            itemList[position].quantity = qty
            Common.cartRepository.updateCart(itemList[position])
            // set the quantity
            holder.qty.text = qty.toString()

            notifyDataSetChanged()

        }

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

            // update database item quantity
            itemList[position].quantity = qty
            Common.cartRepository.updateCart(itemList[position])
            // set the quantity
            holder.qty.text = qty.toString()

            notifyDataSetChanged()
        }

//        adapter.notifyItemChanged(position)


//        holder.qty.text.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(
//                s: CharSequence,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int, count: Int,
//                after: Int
//            ) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                // TODO Auto-generated method stub
//            }
//        })


    }



}