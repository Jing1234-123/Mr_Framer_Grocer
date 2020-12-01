package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mr_framer_grocer.databinding.ActivityMainBinding
import com.example.mr_framer_grocer.databinding.ActivityTestingCheckoutBinding
import com.example.mr_framer_grocer.databinding.ActivityTestingCheckoutBinding.inflate
import com.example.mr_framer_grocer.databinding.ActivityTestingHomeScreenBinding

class TestingCheckout : AppCompatActivity() {
    private lateinit var binding:ActivityTestingCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_testing_checkout)
        binding = ActivityTestingCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkout = binding.button2
        checkout.setOnClickListener {
            intent = Intent(this,Delivery::class.java)
            startActivity(intent)
        }
    }
}