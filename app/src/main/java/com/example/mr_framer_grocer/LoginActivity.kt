package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.mr_framer_grocer.Database.CartDataSource
import com.example.mr_framer_grocer.Database.CartRepository
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.LocalDB.Cart
import com.example.mr_framer_grocer.Database.LocalDB.CartDatabase
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Model.User
import com.example.mr_framer_grocer.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var userInfo: User? = null
    var phoneno: String? = null
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("LOGIN", false)

        if (isRemembered) {
            Common.contact_no = sharedPreferences.getString("PHONE", "")
            Common.psw = sharedPreferences.getString("PASSWORD", "")
            sharedPreferences.getString("NAME", "")

            intent = Intent(this, AllCategory::class.java)
            startActivity(intent)
            finish()
        }

        phoneno = binding.editTextTextPassword.text.toString().trim()

        binding.loginButton.setOnClickListener {
            // if phone number not empty
            if (binding.editTextPhone.text.toString().isNotEmpty()) {
                // if password is not empty
                if (binding.editTextTextPassword.text.toString().isNotEmpty()) {
                    verifyUser(1)
                }
                // if password is empty
                else {
                    /*Toast.makeText(
                        applicationContext,
                        "Please enter your password",
                        Toast.LENGTH_LONG
                    )
                        .show()*/
                    editTextTextPassword.setError("Password Empty")
                }
            }
            // if phone number is empty
            else
            {
                /*Toast.makeText(
                    applicationContext,
                    "Please enter your phone number",
                    Toast.LENGTH_LONG
                ).show()*/
                editTextPhone.setError("Phone Number Empty")
            }
        }

        binding.signUpBtn.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.textViewForgotPassword.setOnClickListener {

            if (binding.editTextPhone.text.toString().isNotEmpty()){
                verifyUser(2)
            }
            else{

                /*Toast.makeText(applicationContext, "Please enter your phone number", Toast.LENGTH_LONG).show()*/
                editTextPhone.setError("Phone Number Empty")
            }
        }
    }

    private fun verifyUser(method: Int){
        //verify user in database
        binding.progress!!.visibility = View.VISIBLE
        val jsonObjectRequest = StringRequest(
            Request.Method.GET, EndPoints.URL_VERIFY_USER + "?contact_no=" + binding.editTextPhone.text.toString(),
            Response.Listener{ response ->
                try {
                    if (response != null) {
                        // get data in JSON format
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                        userInfo = User(
                            jsonResponse.getString("name"),
                            jsonResponse.getString("gender"),
                            jsonResponse.getString("birth_date"),
                            jsonResponse.getString("contact_no"),
                            jsonResponse.getString("email"),
                            jsonResponse.getString("address"),
                            jsonResponse.getString("password")
                        )

                        // if login button
                        if(method == 1)
                        {
                            // verify password, if correct
                            if (binding.editTextTextPassword.text.toString() == userInfo!!.password) {
                                Common.contact_no = editTextPhone.text.toString()
                                Common.psw = editTextTextPassword.text.toString()
                                //val login: Boolean = true

                                //val action:Int = 1

                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("PHONE", Common.contact_no)
                                editor.putString("PASSWORD", Common.psw)
                                editor.putString("NAME", userInfo!!.name)
                                editor.putBoolean("LOGIN", true)
                                editor.apply()

                                Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()

                                // get cart item
                                initDB()
                                Common.cartRepository.emptyCart()
                                getCartItem()

                                intent = Intent(this, AllCategory::class.java)

                                finish()
                                startActivity(intent)
                            }
                            // if password is wrong
                            else {
                                binding.progress!!.visibility = View.GONE
//                                Toast.makeText(applicationContext, "Incorrect password!", Toast.LENGTH_LONG).show()
                                editTextTextPassword.setError("Password Incorrect")
                            }
                        }
                        //if change password button
                        else{
                            intent = Intent(this, PasswordRecoveryActivity::class.java)
                            val phoneNo = "+6$phoneno"
                            intent.putExtra("Phone", phoneNo)
                            startActivity(intent)
                        }

                    } else {
                        binding.progress!!.visibility = View.GONE
                        Toast.makeText(applicationContext, "User not exist", Toast.LENGTH_LONG).show()
                    }

                    binding.progress!!.visibility = View.GONE

                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.progress!!.visibility = View.GONE
                    Toast.makeText(applicationContext, "Connection Lost!", Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener { volleyError ->
                binding.progress!!.visibility = View.GONE
                Toast.makeText(applicationContext, "User not exist", Toast.LENGTH_LONG).show() })

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }

    private fun getCartItem() {

        val jsonObjectRequest = StringRequest(
            Request.Method.GET, EndPoints.URL_READ_CARTITEM + "?contact_no=" + Common.contact_no,
            Response.Listener{ response ->
                try {
                    if (response != null) {
                        // get data in JSON format
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val jsonArray: JSONArray = jsonResponse.getJSONArray("records")
                        val size: Int = jsonArray.length()

                        for (i in 0..size - 1) {
                            val objectProd = jsonArray.getJSONObject(i)
                            val cartitem = Cart(
                                objectProd.getString("id").toInt(),
                                objectProd.getString("name"),
                                objectProd.getString("price").toFloat(),
                                objectProd.getString("weight"),
                                objectProd.getString("image"),
                                objectProd.getString("category"),
                                objectProd.getString("stock").toInt(),
                                objectProd.getString("quantity").toInt()

                            )
                            Common.cartRepository.insertToCart(cartitem)
                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { volleyError ->
                Log.d("Main", "Response: 0")})

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun initDB() {
        Common.cartDatabase = CartDatabase.invoke(this)
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()))
    }
}
