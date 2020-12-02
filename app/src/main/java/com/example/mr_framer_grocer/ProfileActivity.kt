package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.mr_framer_grocer.Adapter.ProfileListAdapter
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Model.ProfileModel
import com.example.mr_framer_grocer.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.miHome -> {
                    val intent = Intent(this, AllCategory::class.java)
                    this.startActivity(intent)
                    finish()
                }
                R.id.miProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    this.startActivity(intent)
                    finish()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val name = preferences.getString("NAME", "")
        //nameTextView.text = name
        nameTextView.text = name

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

                // reset cart item in database
                resetCartDB()
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


    private fun resetCartDB() {

        val url = EndPoints.URL_DELETE_CARTITEM + "?contact_no=" + Common.contact_no

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val success: String = jsonResponse.get("success").toString()

                        if(success.equals("1")){
                            val cartItem = Common.cartRepository.getCartItems()
                            for(i in 0 until cartItem.size)
                            {
                                insertCartItem(cartItem[i].id, cartItem[i].quantity)
                            }
                        }

                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))


                }
            },
            { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))

            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    private fun insertCartItem(id: Int?, quantity:Int?) {
        val url = EndPoints.URL_INSERT_CARTITEM + "?id=" + id + "&contact_no=" + Common.contact_no +
                "&quantity=" + quantity

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val success: String = jsonResponse.get("success").toString()
                        Log.d("Main", "Response: %s".format(success))

                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))


                }
            },
            { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))

            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}