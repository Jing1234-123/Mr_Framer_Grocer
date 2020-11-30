package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.mr_framer_grocer.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    /*lateinit var et_cardholder_name: EditText
    lateinit var et_card_num:EditText
    lateinit var otp_btn:Button*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        /*login()*/
        binding.getOTPBtn.setOnClickListener {
            /*val intent = intent*/
            var phoneno = findViewById<TextView>(R.id.editTextPhone).text.toString().trim()
            var phoneNo = "+6$phoneno"
            binding.editTextOTP.isEnabled = true

            if (!phoneNo.isEmpty()) {
                sendVerificationcode(phoneNo)
            } else {
                Toast.makeText(
                        applicationContext,
                        "Please enter phone number",
                        Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.signUpButton.setOnClickListener {
            var code = binding.editTextOTP.text.toString()

            if (code.isNotEmpty()) {
                verifyVerficationCode(code)
            } else {
                Toast.makeText(
                        applicationContext,
                        "Please enter the OTP sent to your device.",
                        Toast.LENGTH_LONG
                ).show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                Toast.makeText(applicationContext, " Verification Completed!", Toast.LENGTH_LONG).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(applicationContext, "Authentication Failed!", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                Log.d("TAG", "onCodeSent:$verificationId")

                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(applicationContext, "OTP code has been sent to your device!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyVerficationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signup(credential)
    }

    private fun sendVerificationcode(phoneNo: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNo) //Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signup(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (binding.editTextTextPassword.text.toString().isNotEmpty()){
                            if (binding.editTextTextPassword.text.toString().length > 5){
                                if (binding.editTextTextPasswordConfirm.text.toString() == binding.editTextTextPassword.text.toString()){
                                    // Sign in success, update UI with the signed-in user's information
                                    val intent = Intent(applicationContext, MyProfileActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(applicationContext, "Please fill in your details before you proceed", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(applicationContext, "Password and Confirm Password does not match", Toast.LENGTH_LONG).show()
                                }
                            }
                            else{
                                Toast.makeText(applicationContext, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
                            }
                        }
                        else{
                            Toast.makeText(applicationContext, "Please enter your password (6 to 12 characters)", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        // Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(
                                    applicationContext,
                                    "Sign up failed with invalid OTP!",
                                    Toast.LENGTH_LONG
                            ).show()
                            binding.editTextOTP.setText("")
                        }
                    }
                }
    }



}