package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.saveChanges.setOnClickListener {
            if (binding.editTextCurrentPass.text.toString().isNotEmpty()){
                if(binding.editTextNewPass.text.toString().isNotEmpty()){
                    if(binding.editTextNewPass.text.toString().length > 5){
                        if (binding.editTextConfirmPass.text.toString() == binding.editTextNewPass.text.toString()){
                            //update database
                            intent = Intent(this, PasswordChangedSuccessful::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(applicationContext, "New Password and Confirmed Password does not match", Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "New password must be at least 6 characters", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext, "Please enter your new password", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(applicationContext, "Please enter your current password", Toast.LENGTH_LONG).show()
            }
        }
    }
}