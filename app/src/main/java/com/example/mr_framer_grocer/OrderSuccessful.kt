package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mr_framer_grocer.databinding.ActivityOrderSuccessfulBinding

class OrderSuccessful : AppCompatActivity() {
    private lateinit var binding: ActivityOrderSuccessfulBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_order_successful)
        binding = ActivityOrderSuccessfulBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val done = binding.doneBtn
        done.setOnClickListener{
            intent= Intent(this,AllCategory::class.java)
            startActivity(intent)
        }
    }
}