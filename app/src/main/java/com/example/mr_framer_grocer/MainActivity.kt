package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Model.Product
import kotlinx.android.synthetic.main.activity_bottom_nav_bar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_category)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        //intent = Intent(this, productList::class.java)
        //startActivity(intent)

        //       val button: Button = findViewById(R.id.button2)
 //       button.setOnClickListener{

 //           val product = Product(
 //               "Petai", "RM 11.00", "(±200g)",
 //               "https://mrfarmergrocer.com/wp-content/uploads/2020/05/pet.jpg"
//            )

 //           intent = Intent(this, ProductDetailsActivity::class.java)
   //         intent.putExtra("name", product.name)
 //           intent.putExtra("price", product.price)
//            intent.putExtra("weight", product.weight)
//            intent.putExtra("img", product.img)
//            startActivity(intent)
//        }


    }
}

