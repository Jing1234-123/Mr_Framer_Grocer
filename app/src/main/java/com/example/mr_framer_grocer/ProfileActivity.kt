package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.Adapter.ProfileListAdapter
import com.example.mr_framer_grocer.Model.ProfileModel
import com.example.mr_framer_grocer.databinding.ActivityProfileBinding
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val phone = preferences.getString("PHONE", "")
        phoneTv.text = phone


        binding.textViewEditProfile.setOnClickListener {
            intent = Intent(this, MyProfileActivity::class.java)
            intent.putExtra("edit_profile", "yes")
            startActivity(intent)
        }

        //listView
        val listView = findViewById<ListView>(R.id.listview_profile)
        val list = mutableListOf<ProfileModel>()

        //add items into listView
        list.add(ProfileModel(item_name = "My Favourite", photo = R.drawable.my_favourite))
        list.add(ProfileModel(item_name = "My Cart", photo = R.drawable.mycart))
        list.add(ProfileModel(item_name = "About Us", photo = R.drawable.about_us))
        list.add(ProfileModel(item_name = "Contact Us", photo = R.drawable.contact_us))
        list.add(ProfileModel(item_name = "Change Password", photo = R.drawable.change_passw))
        list.add(ProfileModel(item_name = "Logout", photo = R.drawable.logout))

        //adapter
        listView.adapter = ProfileListAdapter(this, R.layout.profile_list_item, list)

        //listView onclick
        listView.setOnItemClickListener { parent, view, position, id ->
            if(position == 1){
                val intent = Intent(this, MyCartActivity::class.java)
                startActivity(intent)
            }
            if (position == 3){
//                val intent = Intent(this, contact_us::class.java)
//                startActivity(intent)
            }
            if (position == 4){
                val intent = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            if (position == 5){
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

       /* listView.setOnItemClickListener { parent, view, position, id ->
            val element = list.elementAt(3)
            val intent = Intent(this, contact_us::class.java)
            startActivity(intent)
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = list.elementAt(4)
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = list.elementAt(5)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }*/

    }
}