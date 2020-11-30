package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if(binding.editTextPhone.text.toString().isNotEmpty()){
                if(binding.editTextPhone.text.toString().length > 9){
                    if (binding.editTextTextPassword.text.toString().isNotEmpty()){
                        if(binding.editTextTextPassword.text.toString().length > 5){
                            intent = Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(applicationContext, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "Please enter your password", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext, "Please enter a valid phone number", Toast.LENGTH_LONG).show()
                }

            }
            else{
                Toast.makeText(applicationContext, "Please enter your phone number", Toast.LENGTH_LONG).show()
            }
        }

        binding.signUpBtn.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.textViewForgotPassword.setOnClickListener {
            if (binding.editTextPhone.text.toString().isNotEmpty()){
                var phoneno = findViewById<TextView>(R.id.editTextPhone).text.toString().trim()
                var phoneNo = "+6$phoneno"

                intent = Intent(this, PasswordRecoveryActivity::class.java)
                intent.putExtra("Phone", phoneNo)
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext, "Please enter your phone number", Toast.LENGTH_LONG).show()
            }
        }

        }
    }
