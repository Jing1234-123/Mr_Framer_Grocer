package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.databinding.ActivityProductListBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bottom_nav_bar.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class productList : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding

    var adapter: ProductAdapters? = null
    var prodList = ArrayList<Product>()
    var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        val bundle = intent.extras
        category = bundle!!.getString("category")


        // set respective category
        binding.pageHeader.text = when (category){
            "Vegetables" -> "Vegetables"
            "Seafoods" -> "Seafoods"
            "Fruits" -> "Fruits"
            "Chicken" -> "Chickens"
            else -> "Eggs"
        }

        // load the product based on category
        loadProduct()

        adapter = ProductAdapters(this, prodList)
        binding.products.adapter = adapter

        // proceed to my cart page
        binding.fab.setOnClickListener{
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }

        // back button

    }

    // get products based on category from server database
    private fun loadProduct() {

        binding.progress.visibility = View.VISIBLE
        val jsonObjectRequest = StringRequest(
            Request.Method.GET, EndPoints.URL_READ_R_PROD + "?category=" + category,
            Response.Listener{ response ->
                try {
                    if (response != null) {
                        // get data in JSON format
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val jsonArray: JSONArray = jsonResponse.getJSONArray("records")
                        val size: Int = jsonArray.length()

                        for (i in 0..size - 1) {
                            val objectProd = jsonArray.getJSONObject(i)
                            val relatedProd = Product(
                                objectProd.getString("id"),
                                objectProd.getString("name"),
                                objectProd.getString("price").toFloat(),
                                objectProd.getString("weight"),
                                objectProd.getString("image"),
                                objectProd.getString("category"),
                                objectProd.getString("stock").toInt()
                            )
                            prodList.add(relatedProd)
                        }


                        val adapter = ProductAdapters(this@productList, prodList)
                        binding.products.adapter = adapter


                    } else {
                        Toast.makeText(applicationContext, "Nothing found in the database", Toast.LENGTH_LONG).show()
                        binding.progress.visibility = View.GONE
                        binding.cnnLost.visibility = View.VISIBLE
                    }
                    binding.progress.visibility = View.GONE
                    binding.cnnLost.visibility = View.GONE
                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.progress.visibility = View.GONE
                    binding.cnnLost.visibility = View.VISIBLE
                }

            }, Response.ErrorListener { volleyError ->
                Toast.makeText(this, "Database not found!", Toast.LENGTH_SHORT).show()
                binding.progress.visibility = View.GONE })

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    // Product List Adapter
    class ProductAdapters(var context: Context, var prodList: ArrayList<Product>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val product = this.prodList[position]
            val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val prodView = inflater.inflate(R.layout.activity_product_list_item, null)

            // Pass the product information
            Glide.with(context).load(product.img).into(prodView.findViewById(R.id.prod_img))
            prodView.findViewById<TextView>(R.id.prodName).text = product.name
            prodView.findViewById<TextView>(R.id.price).text = context.getString(R.string.price, product.price)
            prodView.findViewById<TextView>(R.id.weight).text = context.getString(R.string.weight, product.weight)

            prodView.findViewById<TextView>(R.id.cartbtn).isEnabled = product.stock != 0

            prodView.findViewById<ImageView>(R.id.prod_img).setOnClickListener{
                // if any of the product is clicked, direct to product details page
                val intent = Intent(context, ProductDetailsActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                intent.putExtra("id", product.id)
                intent.putExtra("name", product.name)
                intent.putExtra("price", product.price)
                intent.putExtra("weight", product.weight)
                intent.putExtra("img", product.img)
                intent.putExtra("category", product.category)
                intent.putExtra("stock",product.stock)
                context.startActivity(intent)
            }

            // add to cart
            prodView.findViewById<Button>(R.id.add_to_cart_button).setOnClickListener {
                try {
                    val cartItem = Cart(
                            product.id!!.toInt(),
                            product.name,
                            product.price,
                            product.weight,
                            product.img,
                            product.category,
                            product.stock, 1)

                    //Add to DB
                    Common.cartRepository.insertToCart(cartItem)

                    Log.d("MFG_debug", Gson().toJson(cartItem))

                    Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show()

                } catch (ex: Exception) {
                    val cartItem = Common.cartRepository.getCartItemsById(product.id!!)

                    if (cartItem[0].quantity + 1 > cartItem[0].stock) {
                        Toast.makeText(context, "Quantity reach maximum.", Toast.LENGTH_SHORT).show()
                    } else {
                        val newCart = Cart(
                                product.id!!.toInt(),
                                product.name,
                                product.price,
                                product.weight,
                                product.img,
                                product.category,
                                product.stock, cartItem[0].quantity + 1
                        )

                        Common.cartRepository.updateCart(newCart)

                        Toast.makeText(context, "Quantity +1", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            return prodView
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return prodList.size
        }

    }

}