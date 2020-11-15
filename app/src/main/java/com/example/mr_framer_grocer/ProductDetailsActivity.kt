package com.example.mr_framer_grocer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityProductDetailsBinding

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        binding.plusBtn.setOnClickListener{ plusQty() }
        binding.minusBtn.setOnClickListener{ minusQty() }

    }

    private fun plusQty(){
        val stringInQty = binding.qtyrect.text.toString()
        var qty = stringInQty.toInt()

        qty++

        binding.qtyrect.text = qty.toString()

    }

    private fun minusQty(){
        val stringInQty = binding.qtyrect.text.toString()
        var qty = stringInQty.toInt()

        qty--

        binding.qtyrect.text = qty.toString()

    }
}


