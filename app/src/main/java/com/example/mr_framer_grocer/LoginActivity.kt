package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Model.User
import com.example.mr_framer_grocer.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
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
//                       val exist = verifyUser()
//
//                    if(exist)
//                    {
//                        // verify password, if correct
//                        if(binding.editTextTextPassword.text.toString() == userInfo!!.password)
//                        {
//                            val phone: String = editTextPhone.text.toString()
//                            //val login: Boolean = true
//
//                            //val action:Int = 1
//
//                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                            editor.putString("PHONE", phone)
//                            editor.putBoolean("LOGIN", true)
//                            editor.apply()
//
//                            Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()
//
//                            Common.contact_no = binding.editTextPhone.text.toString()
//                            Common.psw = binding.editTextTextPassword.text.toString()
//                            intent = Intent(this, AllCategory::class.java)
//
//                            finish()
//                            startActivity(intent)
//                        }
//                        // if password is wrong
//                        else
//                        {
//                            binding.progress!!.visibility = View.GONE
//                            Toast.makeText(applicationContext, "Incorrect password!", Toast.LENGTH_LONG).show() }
//                    }


                    //verify user in database
                    binding.progress!!.visibility = View.VISIBLE
                    val jsonObjectRequest = StringRequest(
                        Request.Method.GET,
                        EndPoints.URL_VERIFY_USER + "?contact_no=" + binding.editTextPhone.text.toString(),
                        Response.Listener { response ->
                            try {
                                if (response != null) {
                                    // get data in JSON format
                                    val strResponse = response.toString()
                                    val jsonResponse = JSONObject(strResponse)

                                    Common.userInfo = User(
                                        jsonResponse.getString("name"),
                                        jsonResponse.getString("gender"),
                                        jsonResponse.getString("birth_date"),
                                        jsonResponse.getString("contact_no"),
                                        jsonResponse.getString("email"),
                                        jsonResponse.getString("address"),
                                        jsonResponse.getString("password")
                                    )

                                    // verify password, if correct
                                    if (binding.editTextTextPassword.text.toString() == Common.userInfo!!.password) {
                                        val phone: String = editTextPhone.text.toString()
                                        val editor: SharedPreferences.Editor =
                                            sharedPreferences.edit()
                                        editor.putString("PHONE", phone)
                                        editor.putBoolean("LOGIN", true)
                                        editor.apply()

                                        Toast.makeText(
                                            applicationContext,
                                            "Login successful",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Common.contact_no = binding.editTextPhone.text.toString()
                                        Common.psw = binding.editTextTextPassword.text.toString()
                                        intent = Intent(this, AllCategory::class.java)

                                        finish()
                                        startActivity(intent)
                                    }
                                    // if password is wrong
                                    else {
                                        binding.progress!!.visibility = View.GONE
                                        Toast.makeText(
                                            applicationContext,
                                            "Incorrect password!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }


                                } else {
                                    binding.progress!!.visibility = View.GONE
                                    Toast.makeText(
                                        applicationContext,
                                        "User not exist",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }

                                binding.progress!!.visibility = View.GONE

                            } catch (e: JSONException) {
                                e.printStackTrace()
                                binding.progress!!.visibility = View.GONE
                                Toast.makeText(
                                    applicationContext,
                                    "Connectiob Lost!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        },
                        Response.ErrorListener { volleyError ->
                            binding.progress!!.visibility = View.GONE
                            Toast.makeText(applicationContext, "User not exist", Toast.LENGTH_LONG)
                                .show()
                        })

                    //Volley request policy, only one time request
                    jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        0, //no retry
                        1f
                    )

                    // Access the RequestQueue through your singleton class.
                    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
                }

                // if password is empty
                else {
                    Toast.makeText(
                        applicationContext,
                        "Please enter your password",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
                // if phone number is empty
                else
                {
                    Toast.makeText(
                        applicationContext,
                        "Please enter your phone number",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }



        binding.signUpBtn.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.textViewForgotPassword.setOnClickListener {

            if (binding.editTextPhone.text.toString().isNotEmpty()){
//                val phoneno = findViewById<TextView>(R.id.editTextPhone).text.toString().trim()
                val phoneNo = "+6$phoneno"

                val exist = verifyUser()

                if(exist)
                {
                    intent = Intent(this, PasswordRecoveryActivity::class.java)
                    intent.putExtra("Phone", phoneNo)
                    startActivity(intent)
                }
            }
            else{

                Toast.makeText(applicationContext, "Please enter your phone number", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyUser(): Boolean{
        var exist = false
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

                        exist = true

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

        return exist
    }


}
