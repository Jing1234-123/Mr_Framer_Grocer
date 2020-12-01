package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mr_framer_grocer.databinding.ActivityAboutUsBinding
import com.example.mr_framer_grocer.databinding.ActivityTestingHomeScreenBinding

class TestingHomeScreen : AppCompatActivity() {
    private lateinit var binding:ActivityTestingHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_testing_home_screen)
        binding = ActivityTestingHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contact_us =  binding.contactUs
        contact_us.setOnClickListener{
            intent= Intent(this,ContactUs::class.java)
            startActivity(intent)
        }

        val about_us = binding.aboutUs
        about_us.setOnClickListener{
            intent = Intent(this,AboutUs::class.java)
            startActivity(intent)
        }
    }
}