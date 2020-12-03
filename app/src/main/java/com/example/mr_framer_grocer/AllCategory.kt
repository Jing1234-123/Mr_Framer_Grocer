
package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Database.LocalDB.CartDataSource
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.Database.LocalDB.CartRepository
import com.example.mr_framer_grocer.Database.favRoom.FavDataSource
import com.example.mr_framer_grocer.Database.favRoom.FavDatabase
import com.example.mr_framer_grocer.Database.favRoom.FavRepository
import com.example.mr_framer_grocer.databinding.ActivityAllCategoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AllCategory : AppCompatActivity() {
    private lateinit var binding:ActivityAllCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // cart db
        initDB()
        //fav db
        initFavDB()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.menu.getItem(0).setChecked(true)
        bottomNavigationView.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.miProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    this.startActivity(intent)
                    this.onBackPressed()
                    finish()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

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
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }

    }

    // initialize database
    private fun initDB() {
        Common.cartDatabase = CartDatabase.invoke(this)
        Common.cartRepository = CartRepository.getInstance(
            CartDataSource.getInstance(Common.cartDatabase.cartDAO()))
    }

    private fun initFavDB() {
        Common.favDatabase = FavDatabase.invoke(this)
        Common.favRepository = FavRepository.getInstance(FavDataSource.getInstance(Common.favDatabase.favDao()))
    }

}