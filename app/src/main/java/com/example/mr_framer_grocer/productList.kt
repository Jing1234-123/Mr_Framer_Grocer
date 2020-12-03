package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Database.favRoom.Fav
import com.example.mr_framer_grocer.Database.favRoom.FavDataSource
import com.example.mr_framer_grocer.Database.favRoom.FavDatabase
import com.example.mr_framer_grocer.Database.favRoom.FavRepository
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.databinding.ActivityProductListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
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

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.menu.getItem(1).setChecked(true)

        bottomNavigationView.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.miHome -> {
                    val intent = Intent(this, AllCategory::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    this.startActivity(intent)
                    finish()
                }
                R.id.miProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    this.startActivity(intent)
                    finish()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        initFavDB()

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
        binding.backbtn.setOnClickListener{
            val intent = Intent(this, AllCategory::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

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
                Toast.makeText(this, "Connection Lost!", Toast.LENGTH_SHORT).show()
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

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val product = this.prodList[position]
            val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val prodView = inflater.inflate(R.layout.activity_product_list_item, null)

            // Pass the product information
            Glide.with(context).load(product.img).into(prodView.findViewById(R.id.prod_img))
            prodView.findViewById<TextView>(R.id.prodName).text = product.name
            prodView.findViewById<TextView>(R.id.price).text = context.getString(R.string.price, product.price)
            prodView.findViewById<TextView>(R.id.weight).text = context.getString(R.string.weight, product.weight)

            val cartBtn = prodView.findViewById<Button>(R.id.add_to_cart_button)
            val favBtn = prodView.findViewById<Button>(R.id.favBtn)

            val favItem = Common.favRepository.getFavItems()

            for(i in 0 until favItem.size)
            {
                if(product.id == favItem[i].id.toString())
                    favBtn.setBackgroundResource(R.drawable.prod_list_fill_heart)
                else
                    favBtn.setBackgroundResource(R.drawable.prod_list_emp_heart)
            }

            if(product.stock == 0)
            {
                cartBtn.isEnabled = false
                cartBtn.alpha = 0.5f
            }
            else{
                cartBtn.isEnabled = true
                cartBtn.alpha = 1.0f
            }


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
                        Toast.makeText(context, "Quantity exceed stock level.", Toast.LENGTH_SHORT).show()
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

                        Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // add to fav
            favBtn.setOnClickListener {
                if(favBtn.background.constantState == context.getDrawable(R.drawable.prod_list_emp_heart)!!.constantState)
                {
                    favBtn.setBackgroundResource(R.drawable.prod_list_fill_heart)
                    val newFav = Fav(
                        product.id!!.toInt(),
                        product.name,
                        product.price,
                        product.weight,
                        product.img,
                        product.category,
                        product.stock)

                    // add to database
                    Common.favRepository.addToFav(newFav)
                    Log.d("fav_table", Gson().toJson(newFav))
                    Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    favBtn.setBackgroundResource(R.drawable.prod_list_emp_heart)
                    // remove from database
                    Common.favRepository.delById(product.id!!.toInt())
                    Toast.makeText(context, "Removed from Favorite", Toast.LENGTH_SHORT).show()
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

    private fun initFavDB() {
        Common.favDatabase = FavDatabase.invoke(this)
        Common.favRepository = FavRepository.getInstance(FavDataSource.getInstance(Common.favDatabase.favDao()))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miHome -> {
                val intent = Intent(this, AllCategory::class.java)
                this.startActivity(intent)
            }
            R.id.miProfile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                this.startActivity(intent)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }*/

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.background = null
        adapter = ProductAdapters(this, prodList)
        binding.products.adapter = adapter

    }
}
