package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityPaymentSuccessfulBinding

class PaymentSuccessful : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentSuccessfulBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_order_successful)
        binding = ActivityPaymentSuccessfulBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val done = binding.doneBtn
        done.setOnClickListener{
            intent= Intent(this,AllCategory::class.java)
            startActivity(intent)
            finish()
        }
    }
}