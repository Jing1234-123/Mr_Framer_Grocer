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
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Helper.MyButton
import com.example.mr_framer_grocer.Helper.MyButtonClickListener
import com.example.mr_framer_grocer.Helper.MySwipeHelper
import com.example.mr_framer_grocer.databinding.ActivityMyCartBinding
import com.google.android.material.snackbar.Snackbar


class MyCartActivity : AppCompatActivity() {

    lateinit var cartAdapter: MyAdapter
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMyCartBinding
    var itemList = ArrayList<Cart>()
    var totalprice = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCartItem()


        // if the my cart is empty
        if(itemList.isNullOrEmpty()) {
            setContentView(R.layout.empty_cart)
            val back_menu = findViewById<Button>(R.id.back_menu_btn)
            back_menu.setOnClickListener {
                // Back to menu page
                finish()
                val intent = Intent(this, AllCategory::class.java)
                startActivity(intent)

            }
        }
        // if the cart have added items
        else {

            binding.cartList.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this)
            binding.cartList.layoutManager = layoutManager

            cartAdapter = MyAdapter(this, itemList)
            binding.cartList.adapter = cartAdapter

            cartAdapter.setOnDataChangeListener(object : MyAdapter.OnDataChangeListener {
                override fun onDataChanged() {

                    totalprice = 0f

                    for(i in 0..itemList.size - 1)
                    {
//
                        totalprice += itemList[i].price!! * itemList[i].quantity
                    }

                    binding.totalprice.text = getString(R.string.price, totalprice)
                }
            })


            val swipe = object : MySwipeHelper(this, binding.cartList) {
                override fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<MyButton>) {
                    buffer.add(MyButton(
                            "Delete",
                            0,
                            Color.parseColor("#42995C"),
                            object : MyButtonClickListener {
                                override fun onClick(pos: Int) {

                                    val tmp = cartAdapter.itemList[pos] //temporary variable
                                    val id = cartAdapter.itemList[pos].id
                                    totalprice -= cartAdapter.itemList[pos].price!! * cartAdapter.itemList[pos].quantity
                                    binding.totalprice.text = getString(R.string.price, totalprice)
                                    cartAdapter.itemList.removeAt(pos)
                                    cartAdapter.notifyItemRemoved(pos)
                                    cartAdapter.notifyItemRangeChanged(pos, cartAdapter.itemCount)
                                    updateCartCount()


                                    val snackbar =
                                        Snackbar.make(binding.cartList, "1 item has been removed.", Snackbar.LENGTH_LONG)
                                            .setAction(
                                                "UNDO"
                                            ) {
                                                cartAdapter.itemList.add(pos, tmp)
                                                totalprice += cartAdapter.itemList[pos].price!! * cartAdapter.itemList[pos].quantity
                                                binding.totalprice.text = getString(R.string.price, totalprice)
                                                updateCartCount()
                                                cartAdapter.notifyDataSetChanged()

                                            }.setCallback(object : Snackbar.Callback() {
                                                override fun onDismissed(
                                                    snackbar: Snackbar,
                                                    dismissType: Int
                                                ) {
                                                    super.onDismissed(snackbar, dismissType)

                                                    //if the dismiss type is not undo, delete the cart item from database
                                                    if (dismissType == DISMISS_EVENT_TIMEOUT || dismissType == DISMISS_EVENT_SWIPE || dismissType == DISMISS_EVENT_CONSECUTIVE || dismissType == DISMISS_EVENT_MANUAL)
                                                    {
                                                        // Delete the cart item in room database
                                                        Common.cartRepository.deleteCartItemById(id.toString())
                                                        updateCartCount()
                                                    }

                                                }
                                            })
                                    snackbar.show()
                                }

                            }

                    ))
             }

            }

                // calculate total price of all cart items
                for(i in 0..cartAdapter.itemList.size - 1)
                {
                    totalprice += cartAdapter.itemList[i].price!! * cartAdapter.itemList[i].quantity
                }

                binding.totalprice.text = getString(R.string.price, totalprice)

            binding.emptyCart.setOnClickListener{
                // ask for confirmation
                    alertDialog()
            }
        }
    }

    private fun loadCartItem() {
        itemList = ArrayList(Common.cartRepository.getCartItems())
    }

    private fun updateCartCount() {
        runOnUiThread {
            binding.totalItem.text = getString(R.string.totalItem, Common.cartRepository.countCartItems())
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartCount()
    }

    private fun alertDialog() {
        val dialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        dialog.setMessage("Are you sure you want to empty the cart?")
        dialog.setTitle("Confirmation")

        dialog.setNegativeButton("Yes"
        ) { _, _ ->
            Common.cartRepository.emptyCart()
            // refresh the screen
            finish()
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)

            Toast.makeText(applicationContext, "The cart is cleared", Toast.LENGTH_LONG)
                .show()
        }
        dialog.setPositiveButton("Cancel")
        { _, _ ->
                Toast.makeText(applicationContext, "Clear action is cancelled", Toast.LENGTH_LONG)
                    .show()
            }

        val alertDialog: android.app.AlertDialog? = dialog.create()
        alertDialog!!.show()
    }

}
