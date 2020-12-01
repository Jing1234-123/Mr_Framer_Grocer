package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mr_framer_grocer.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveChanges.setOnClickListener {
            if (binding.editTextNewPass.text.toString().isNotEmpty()){
                if (binding.editTextConfirmPass.text.toString() == binding.editTextNewPass.text.toString()){
                    intent = Intent(this, PasswordResetSuccessful::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(applicationContext, "New Password and Confirmed Password does not match", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(applicationContext, "Please enter new password", Toast.LENGTH_LONG).show()
            }
        }
    }
}