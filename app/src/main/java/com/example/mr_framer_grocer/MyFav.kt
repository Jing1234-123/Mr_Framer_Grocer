package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mr_framer_grocer.Adapter.FavAdapter
import com.example.mr_framer_grocer.Database.favRoom.Fav
import com.example.mr_framer_grocer.databinding.ActivityMyFavBinding


class MyFav : AppCompatActivity() {

    lateinit var favAdapter: FavAdapter
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding:ActivityMyFavBinding
    var favListItem = ArrayList<Fav>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_my_fav)

        binding = ActivityMyFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        loadFavItem()

        binding.backBtn!!.setOnClickListener {
            intent= Intent(this, ProfileActivity::class.java)  /////////////////------------->>>>>>>>>>>>>>>> HERE
            startActivity(intent)
            finish()
        }
//
//        // if fav list has nothing
//        if(favListItem.isNullOrEmpty()) {
//            Toast.makeText(this, "Nothing inside favorite list", Toast.LENGTH_SHORT).show()
//        }
//        else {
//            binding.favList.setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(this)
//            binding.favList.layoutManager = layoutManager
//
//            favAdapter = FavAdapter(this, favListItem )
//            binding.favList.adapter = favAdapter
//        }

    }

    private fun loadFavItem() {
        favListItem = ArrayList(Common.favRepository.getFavItems())
    }

    override fun onResume() {
        super.onResume()

        loadFavItem()

        binding.favList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        binding.favList.layoutManager = layoutManager

        favAdapter = FavAdapter(this, favListItem )
        binding.favList.adapter = favAdapter

         //if fav list has nothing
        if(favListItem.isNullOrEmpty()) {
            Toast.makeText(this, "Nothing inside favorite list", Toast.LENGTH_SHORT).show()
        }



    }

}