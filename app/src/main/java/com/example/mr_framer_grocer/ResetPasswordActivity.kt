package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.databinding.ActivityResetPasswordBinding
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveChanges.setOnClickListener {
            if (binding.editTextNewPass.text.toString().isNotEmpty()){
                if (binding.editTextConfirmPass.text.toString() == binding.editTextNewPass.text.toString()){
                    //update password
                    updatePsw()
                    intent = Intent(this, PasswordResetSuccessful::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    editTextConfirmPass.setError("Password and Confirm Password does not match")
                }
            }
            else{
                editTextNewPass.setError("New Password is Empty")
            }
        }
    }

    private fun updatePsw() {
        val url = EndPoints.URL_UPDATEPSW_USER + "?password=" + binding.editTextNewPass.text.toString() +
                "&contact_no=" + Common.contact_no

        binding.progress.visibility = View.VISIBLE

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
                            Toast.makeText(applicationContext, "Changed password successfully!", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(applicationContext, "Fail to change password", Toast.LENGTH_LONG).show()
                        }
                        binding.progress.visibility = View.GONE
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))
                    binding.progress.visibility = View.GONE

                }
            },
            { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
                binding.progress.visibility = View.GONE
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