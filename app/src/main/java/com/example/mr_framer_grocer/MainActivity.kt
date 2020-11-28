package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Model.Product
import com.example.mr_framer_grocer.databinding.ActivityAboutUsBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(this, TestingCheckout::class.java)
        startActivity(intent)

//        setContentView(R.layout.activity_main)
//
//        val button: Button = findViewById(R.id.button2)
//        button.setOnClickListener{
//
//            val product = Product(
//                "Petai", "RM 11.00", "(±200g)",
//                "https://mrfarmergrocer.com/wp-content/uploads/2020/05/pet.jpg"
//            )
//
//            intent = Intent(this, ProductDetailsActivity::class.java)
//            intent.putExtra("name", product.name)
//            intent.putExtra("price", product.price)
//            intent.putExtra("weight", product.weight)
//            intent.putExtra("img", product.img)
//            startActivity(intent)
//        }



//        intent = Intent(this, Testing::class.java)
//        startActivity(intent)



    }
}

