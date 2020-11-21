package com.example.mr_framer_grocer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_framer_grocer.Adapter.MyAdapter
import com.example.mr_framer_grocer.Helper.MyButton
import com.example.mr_framer_grocer.Helper.MySwipeHelper
import com.example.mr_framer_grocer.Listener.MyButtonClickListener
import com.example.mr_framer_grocer.Model.Item
import com.example.mr_framer_grocer.databinding.ActivityMyCartBinding



class MyCartActivity : AppCompatActivity() {

    lateinit var cartAdapter: MyAdapter
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMyCartBinding
    val itemList = ArrayList<Item?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        generateItem()

        // if the my cart is empty
        if(itemList.isNullOrEmpty()) {
            setContentView(R.layout.empty_cart)
            val back_menu = findViewById<Button>(R.id.back_menu_btn)
            back_menu.setOnClickListener {
                // Back to menu page
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        // if the cart have added items
        else {
            binding = ActivityMyCartBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.cartList.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this)
            binding.cartList.layoutManager = layoutManager

            cartAdapter = MyAdapter(this, itemList)
            binding.cartList.adapter = cartAdapter


            //add swipe
            val swipe = object : MySwipeHelper(this, binding.cartList, 200) {
                override fun instantiateMyButton(
                        viewHolder: RecyclerView.ViewHolder,
                        buffer: MutableList<MyButton>
                ) {
                    buffer.add(
                            MyButton(this@MyCartActivity,
                                    "Delete",
                                    30,
                                    0,
                                    Color.parseColor("#FF03DAC5"),
                                    object : MyButtonClickListener {

                                        //when click the delete button
                                        override fun onClick(pos: Int) {
                                            Toast.makeText(this@MyCartActivity, "DELETE ID" + pos, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            ))
                    buffer.add(
                            MyButton(this@MyCartActivity,
                                    "Like",
                                    30,
                                    R.drawable.cart_empty_heart,
                                    Color.parseColor("#FFBB86FC"),

                                    //when click the like button
                                    object : MyButtonClickListener {
                                        override fun onClick(pos: Int) {

                                            Toast.makeText(this@MyCartActivity, "LIKE ID" + pos, Toast.LENGTH_SHORT).show()
                                        }

                                    }
                            ))
                }
            }



        }
    }


    fun generateItem() {
//        itemList.add(Item(item.name, item.price, item.image))

        var i = 0
        while(i<20)
        {
            itemList.add(Item("Tomato",
            "RM 9.99",
            "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
            i++
        }
    }



}