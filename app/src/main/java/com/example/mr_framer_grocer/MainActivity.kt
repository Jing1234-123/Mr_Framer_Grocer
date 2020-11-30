package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Database.CartDataSource
import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDB()
        val button: Button = findViewById(R.id.button2)
        button.setOnClickListener {

            intent = Intent(this, AllCategory::class.java)
            startActivity(intent)

//            val product = Product("80",
//                    "Petai", 11f, "200",
//                    "https://mrfarmergrocer.com/wp-content/uploads/2020/05/pet.jpg", "Vegetables", 15
//            )
//
//
//
//            intent = Intent(this, ProductDetailsActivity::class.java)
//            intent.putExtra("id", product.id)
//            intent.putExtra("name", product.name)
//            intent.putExtra("price", product.price)
//            intent.putExtra("weight", product.weight)
//            intent.putExtra("img", product.img)
//            intent.putExtra("category", product.category)
//            intent.putExtra("stock", product.stock)
//
//            startActivity(intent)

        }


    }

    private fun initDB() {
        Common.cartDatabase = CartDatabase.invoke(this)
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()))
    }

}