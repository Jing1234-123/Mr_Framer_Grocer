package com.example.mr_framer_grocer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Database.favRoom.*
import com.example.mr_framer_grocer.databinding.ActivityProductListItemBinding

class ProductListItem : AppCompatActivity() {
    private lateinit var binding: ActivityProductListItemBinding
    //private lateinit var todoDao: FavDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_product_list_item)
        binding = ActivityProductListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFavDB()

    }

    private fun initFavDB() {
        Common.favDatabase = FavDatabase.invoke(this)
        Common.favRepository = FavRepository.getInstance(FavDataSource.getInstance(Common.favDatabase.favDao()))
    }


}