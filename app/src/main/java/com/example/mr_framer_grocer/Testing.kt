package com.example.mr_framer_grocer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mr_framer_grocer.Model.Product

class Testing : AppCompatActivity() {

    var adapter: ProdAdapters? = null
    var prodList = ArrayList<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        // Related Products
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))
//        prodList.add(Product("Tomato", "RM 3.60", "(±500g)", "https://mrfarmergrocer.com/wp-content/uploads/2020/04/Tomato-1.jpg"))

        adapter = ProdAdapters(this, prodList)
//        var relatedProdList = findViewById<LinearLayout>(R.id.related_prod_list).adapter
        var relatedProdList = findViewById<GridView>(R.id.related)
        relatedProdList.adapter = adapter

    }



    // Related Product List
    class ProdAdapters: BaseAdapter {
        var context: Context? = null
        var prodList = ArrayList<Product>()

        constructor(context: Context?, prodList: ArrayList<Product>) : super() {
            this.prodList = prodList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var product = this.prodList[position]
            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var prodView = inflater.inflate(R.layout.product_layout, null)

            // Pass the product information
            Glide.with(context!!).load(product.img).into(prodView.findViewById(R.id.prod_img))
            prodView.findViewById<TextView>(R.id.prodName).text = product.name
            prodView.findViewById<TextView>(R.id.price).text = product.price.toString()
            prodView.findViewById<TextView>(R.id.weight).text = product.weight

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




