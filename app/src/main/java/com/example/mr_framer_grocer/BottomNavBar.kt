package com.example.mr_framer_grocer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bottom_nav_bar.*

class BottomNavBar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav_bar)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

    }


}