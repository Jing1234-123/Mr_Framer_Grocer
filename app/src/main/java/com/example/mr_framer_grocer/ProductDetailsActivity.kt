package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.mr_framer_grocer.Adapter.CustomExpandableListAdapter
import com.example.mr_framer_grocer.Adapter.ProdAdapter
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Database.LocalDB.CartDataSource
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.Database.LocalDB.CartRepository
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Database.favRoom.Fav
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.databinding.ActivityProductDetailsBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.random.Random


class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    // Product details
    private var id: String? = null
    private var name: String? = null
    private var price = 0f
    private var weight: String? = null
    private var image:  String? = null
    private var category: String? = null
    private var stock = 0

    var quantity = 1

    //expandable list
    private lateinit var listViewAdapter: CustomExpandableListAdapter
    private lateinit var titleList: List<String>
    private lateinit var listDetail: HashMap<String, List<String>>
    private var add_cart_item: Product? = null

    // Related Product List
    lateinit var layoutManager: LinearLayoutManager
    var prodList = ArrayList<Product>()
    var ramprodList = ArrayList<Product>()
    lateinit var relatedProdadapter: ProdAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get product details
        val bundle = intent.extras

        id = bundle!!.getString("id")
        name = bundle.getString("name")
        price = bundle.getFloat("price")
        weight = bundle.getString("weight")
        image = bundle.getString("img")
        category = bundle.getString("category")
        stock = bundle.getInt("stock")

        // put info into view
        binding.prodName.text = name
        binding.price.text = this.getString(R.string.price, price)
        binding.weight.text = this.getString(R.string.weight, weight)
        binding.stock.text = this.getString(R.string.stock,stock)
        // load image using url link in string format
        Picasso.get().load(image).into(binding.prodImage)

        // initiate the cart
        initDB()

        loadRelatedProd()
        binding.relatedProdList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.relatedProdList.layoutManager = layoutManager

        relatedProdadapter = ProdAdapter(this, prodList)
        binding.relatedProdList.adapter = relatedProdadapter

        // if stock is 0
        if(stock == 0)
        {
            binding.plusBtn.isEnabled = false
            binding.plusBtn.alpha = 0.5f
            binding.minusBtn.isEnabled = false
            binding.minusBtn.alpha = 0.5f
            binding.addToCartButton.isEnabled = false
            binding.addToCartButton.alpha = 0.5f
            binding.buyNowButton.isEnabled = false
            binding.buyNowButton.alpha = 0.5f
            binding.stock.text = "Out of stock"
        }

        // handle qty plus and minus button
        binding.plusBtn.setOnClickListener { plusQty() }
        binding.minusBtn.setOnClickListener { minusQty() }

        // expandable list
        val expandableListView = binding.expandableList
        showList()
        listViewAdapter = CustomExpandableListAdapter(this, titleList, listDetail)
        expandableListView.setAdapter(listViewAdapter)
        expandableListView.setOnGroupClickListener { parent, v, groupPosition, id ->
            setListViewDetailHeight(parent, groupPosition)
            false
        }

        // navigate to my cart activity
        binding.cartIcon.setOnClickListener {
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }

        val favItem = Common.favRepository.getFavItems()

        for(i in 0 until favItem.size)
        {
            if(id == favItem[i].id.toString())
              binding.heartButton.setBackgroundResource(R.drawable.filled_heart)

        }

        // when the user click the empty heart, chg the heart to filled heart
        binding.heartButton.setOnClickListener {
            // if the current heart is empty, chg to fill heart and add item to favourite and vice versa
            if (binding.heartButton.background.constantState == resources.getDrawable(R.drawable.empty_heart).constantState){
                // LIKE
                binding.heartButton.setBackgroundResource(R.drawable.filled_heart)
                val newFav = Fav(
                    id!!.toInt(), name, price, weight, image, category,stock)

                // add to database
                Common.favRepository.addToFav(newFav)
                Log.d("fav_table", Gson().toJson(newFav))
                Toast.makeText(this, "Added to Favourite", Toast.LENGTH_SHORT).show()
            }
            else{
                // DISLIKE
                binding.heartButton.setBackgroundResource(R.drawable.empty_heart)

                // remove from database
                Common.favRepository.delById(id!!.toInt())
                Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_SHORT).show()
            }

        }

        // Add item to cart action
        binding.addToCartButton.setOnClickListener {
            // Add item to the local cart database(SQLite)
            //Create new cart item
            try {
                val cartItem = Cart(id!!.toInt(), name, price, weight
               ,image, category, stock, quantity)

                //Add to DB
                Common.cartRepository.insertToCart(cartItem)

                Log.d("MFG_debug", Gson().toJson(cartItem))

                Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                val cartItem = Common.cartRepository.getCartItemsById(id!!)

                if(cartItem[0].quantity + quantity > cartItem[0].stock)
                {
                    Toast.makeText(this, "Quantity exceed stock level", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val newCart = Cart(id!!.toInt(), name, price, weight
                        ,image, category, stock,cartItem[0].quantity+quantity)

                    Common.cartRepository.updateCart(newCart)

                    Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show()
                }

            }

            updateCartCount()

        }


        // buy now
        binding.buyNowButton.setOnClickListener{
            val intent = Intent(this, Delivery::class.java)
            intent.putExtra("method", "buynow" )
            intent.putExtra("subtotal", price*quantity)
            intent.putExtra("prodID", id)
            intent.putExtra("newstock", stock-quantity)

            startActivity(intent)
        }

        // back button
        binding.backArrow.setOnClickListener{
            val cate = when(category){
                "Vegetables" -> "Vegetables"
                "Seafoods" -> "Seafoods"
                "Fruits" -> "Fruits"
                "Chicken" -> "Chickens"
                else -> "Eggs"
            }
            val intent = Intent(this, productList::class.java)
            intent.putExtra("category", cate)
            finish()
            startActivity(intent)
        }


    }

    private fun updateCartCount() {
        runOnUiThread {
            val max = "99+"

            if(Common.cartRepository.countCartItems() > 99) {
                binding.cartItemCounter.text = max
                binding.cartItemCounter.textSize =8F
            } else {
                binding.cartItemCounter.textSize =10F
                binding.cartItemCounter.text = Common.cartRepository.countCartItems().toString()
            }
        }

    }

    private fun initDB() {
        Common.cartDatabase = CartDatabase.invoke(this)
        Common.cartRepository = CartRepository.getInstance(
            CartDataSource.getInstance(Common.cartDatabase.cartDAO()))
    }

    // get related products based on category from server database
    private fun loadRelatedProd() {

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

                        // remove the current product from the list
                        for(x in 0 until prodList.size){
                            
                            if(prodList[x].name == binding.prodName.text.toString())
                            {
                                prodList.removeAt(x)
                                break
                            }
                        }

                        // display 5 product from the whole product list
                        if(prodList.size > 10)
                        {
                            for(i in 0..10)
                            {
                                val randomIndex = Random.nextInt(prodList.size)
                                ramprodList.add(prodList.get(randomIndex))
                                prodList.removeAt(randomIndex)
                            }
                        }
                        else{
                            ramprodList = prodList
                        }

                        val adapter = ProdAdapter(this@ProductDetailsActivity, ramprodList)
                        binding.relatedProdList.adapter = adapter
                        adapter.setOnItemChangeListener(object : ProdAdapter.OnItemChangeListener {
                            override fun onItemChanged() {
                                updateCartCount()
                            }
                        })

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
                 binding.progress.visibility = View.GONE})
//        Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    // handle quantity add button
    private fun plusQty() {
        val stringInQty = binding.qtyrect.text.toString()
        var qty = stringInQty.toInt()

        // restore the minus btn
        binding.minusBtn.isEnabled = true
        binding.minusBtn.alpha = 1.0f

        // if qty reach the number of left stock, disable and fade the plus button
        if (binding.plusBtn.isEnabled) {
            binding.plusBtn.alpha = 1.0f
            qty++

            if (qty == stock) {
                binding.plusBtn.isEnabled = false
                binding.plusBtn.alpha = 0.5f
            }
        }

        // set the quantity
        quantity = qty
        binding.qtyrect.text = qty.toString()
    }

    // handle quantity minus button
    private fun minusQty() {
        val stringInQty = binding.qtyrect.text.toString()
        var qty = stringInQty.toInt()

        // restore the minus
        binding.plusBtn.isEnabled = true
        binding.plusBtn.alpha = 1.0f

        // the qty cannot less then 1, disable the button when qty is 1
        if (binding.minusBtn.isEnabled) {
            binding.minusBtn.alpha = 1.0f
            qty--

            if (qty == 1) {
                binding.minusBtn.isEnabled = false
                binding.minusBtn.alpha = 0.5f
            }
        }

        // set the quantity
        quantity = qty
        binding.qtyrect.text = qty.toString()

    }

    // Expandable list information
    private fun showList() {
        titleList = ArrayList()
        listDetail = HashMap()


        // Add title (parent)
        (titleList as ArrayList<String>).add("Terms & Conditions")
        (titleList as ArrayList<String>).add("Additional Information")

        // Add child list view
        val info1: MutableList<String> = ArrayList()
        info1.add(
            "1) Currently we only accept delivery to Klang Valley area\n" +
                    "\n" +
                    "2) Vegetables & fruits which are out of stock will be replaced by any \n" +
                    "of the fruits or vegetables with the same value available in store.\n" +
                    "\n" +
                    "3) With purchase below RM 80, the delivery fee will charge at RM10;\n" +
                    "RM 80 & above, we will provide free delivery.\n" +
                    "\n" +
                    "4) Orders placed before 6pm will be delivered on the next day before \n" +
                    "6pm."
        )

        val info2: MutableList<String> = ArrayList()
        info2.add("Weight\t\t${weight}")

        listDetail[titleList[0]] = info1
        listDetail[titleList[1]] = info2
    }

    // to disable scrolling in child of expendable list
    private fun setListViewDetailHeight(listView: ExpandableListView, group: Int) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth: Int = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem: View = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem: View = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    override fun onResume() {
        super.onResume()
        updateCartCount()

        loadRelatedProd()
        relatedProdadapter = ProdAdapter(this, prodList)
        binding.relatedProdList.adapter = relatedProdadapter
    }


}





