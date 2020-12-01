
package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Database.CartDataSource
import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.databinding.ActivityAllCategoryBinding
import kotlinx.android.synthetic.main.activity_bottom_nav_bar.*


class AllCategory : AppCompatActivity() {
    private lateinit var binding: ActivityAllCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_all_category)

        initDB()

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        binding.vegeTab.setOnClickListener{
            val intent = Intent(this, productList::class.java)
            intent.putExtra("category", "Vegetables")
            startActivity(intent)
        }

        binding.fruitsTab.setOnClickListener{
            val intent = Intent(this, productList::class.java)
            intent.putExtra("category", "Fruits")
            startActivity(intent)
        }

        binding.seafoodsTab.setOnClickListener{
            val intent = Intent(this, productList::class.java)
            intent.putExtra("category", "Seafoods")
            startActivity(intent)
        }

        binding.chickensTab.setOnClickListener{
            val intent = Intent(this, productList::class.java)
            intent.putExtra("category", "Chickens")
            startActivity(intent)
        }

        binding.eggsTab.setOnClickListener{
            val intent = Intent(this, productList::class.java)
            intent.putExtra("category", "Eggs")
            startActivity(intent)
        }

        // proceed to my cart page
        binding.fab.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


    }

    // initialize database
    private fun initDB() {
        Common.cartDatabase = CartDatabase.invoke(this)
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()))
    }

}