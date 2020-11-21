package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mr_framer_grocer.Adapter.ProdAdapter
import com.example.mr_framer_grocer.Model.Item
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.databinding.ActivityProductDetailsBinding
import com.squareup.picasso.Picasso


class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    val stock = 10

    //expandable list
    private lateinit var listViewAdapter: CustomExpandableListAdapter
    private lateinit var titleList: List<String>
    private lateinit var listDetail: HashMap<String, List<String>>
    private var add_cart_item: Item? = null

    // Related Product List
    lateinit var layoutManager: LinearLayoutManager
    var relatedProdadapter: ProdAdapter? = null
    var prodList = ArrayList<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get product details
        val bundle = intent.extras
        binding.prodName.text = bundle!!.getString("name")
        binding.price.text = bundle.getString("price")
        binding.weight.text = bundle.getString("weight")
        // load image using url link in string format
        Picasso.get().load(bundle.getString("img")).into(binding.prodImage)


        // handle qty plus and minus button
        binding.plusBtn.setOnClickListener { plusQty() }
        binding.minusBtn.setOnClickListener { minusQty() }

        // expandable list
        val expandableListView = binding.expandableList
        showList()
        listViewAdapter = CustomExpandableListAdapter(this, titleList, listDetail)
        expandableListView.setAdapter(listViewAdapter)
        expandableListView!!.setOnGroupClickListener { parent, v, groupPosition, id ->
            setListViewDetailHeight(parent, groupPosition)
            false
        }

        // navigate to my cart activity
        binding.cartIcon.setOnClickListener {
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }

        // Related Products
        prodList.add(
            Product(
                "Tomato",
                "RM 3.60",
                "(±500g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"
            )
        )
        prodList.add(
            Product(
                "Petai",
                "RM 11.00",
                "(±200g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/05/pet.jpg"
            )
        )
        prodList.add(
            Product(
                "Terung Pendek",
                "RM 3.50",
                "(±500g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Terung-Pendek-1.jpg"
            )
        )
        prodList.add(
            Product(
                "Lady’s Finger",
                "RM 3.60",
                "(±500g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/05/Lady-Finger.jpg"
            )
        )
        prodList.add(
            Product(
                "Timun Tua",
                "RM 7.00",
                "(±800g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Timun-Tua-1-768x768.jpg"
            )
        )
        prodList.add(
            Product(
                "Cauliflower",
                "RM 6.50",
                "(±800g/ pc)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/05/Cauliflower.jpg"
            )
        )
        prodList.add(
            Product(
                "TChives",
                "RM 5.00",
                "(±180g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/05/Kucai.jpeg"
            )
        )
        prodList.add(
            Product(
                "Shimeiji White testinmg",
                "RM 2.90",
                "(±150g)",
                "https://mrfarmergrocer.com/wp-content/uploads/2020/05/Shimeiji-White.jpg"
            )
        )

        binding.relatedProdList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.relatedProdList.layoutManager = layoutManager
        relatedProdadapter = ProdAdapter(this, prodList)
        binding.relatedProdList.adapter = relatedProdadapter

        // when the user click the empty heart, chg the heart to filled heart
        binding.heartButton.setOnClickListener {
            // if the current heart is empty, chg to fill heart and add item to favourite and vice versa
            if (binding.heartButton.background.constantState == resources.getDrawable(R.drawable.empty_heart).constantState)
            // LIKE
                binding.heartButton.setBackgroundResource(R.drawable.filled_heart)
            else
            // DISLIKE
                binding.heartButton.setBackgroundResource(R.drawable.empty_heart)
        }

        // Add item to cart action
        binding.addToCartButton.setOnClickListener {
//            // Add item to the cart list

            // increase cart item counter
            val cartItemCounter = CartItemCounter()
            cartItemCounter.cartItemCounter(binding.cartItemCounter)
            cartItemCounter.increaseNum()
        }

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
        info2.add("Weight\t0.5kg")

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

    class CartItemCounter {
        lateinit var cartItemNumber: TextView
        val maxNum = 10
        var max = "99+"
        var counter = 1

        fun cartItemCounter(view: View){
            cartItemNumber = view.findViewById(R.id.cartItemCounter)
        }

        fun increaseNum(){
            counter++

            if(counter > maxNum){
                cartItemNumber.text = max
                Log.d("Counter", "Maximum number for cart items counter")
            }
            else{
                cartItemNumber.text = counter.toString()
            }
        }
    }


}





