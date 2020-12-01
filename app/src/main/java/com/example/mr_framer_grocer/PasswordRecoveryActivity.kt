package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.mr_framer_grocer.databinding.ActivityPasswordRecoveryBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PasswordRecoveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordRecoveryBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        //Forget Password

        //get data from intent
        var intent = intent
        var phoneNo = intent.getStringExtra("Phone")

        //textView
        var phoneNum = findViewById<TextView>(R.id.textViewPhoneNumber)
        //setText
        phoneNum.text = phoneNo

        binding.textViewPhoneNumber.setOnClickListener {
            var phoneNumber = phoneNum.text.toString().trim()

            binding.recoveryCode.isEnabled = true

            if (!phoneNumber.isEmpty()) {
                sendVerificationcode(phoneNumber)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter phone number",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.proceedButton.setOnClickListener {
            var code = binding.recoveryCode.text.toString()

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

    private fun sendVerificationcode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) //Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyVerficationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        passwordRecovery(credential)
    }

    private fun passwordRecovery(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(applicationContext, ResetPasswordActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(
                            applicationContext,
                            "Sign up failed with invalid OTP!",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.recoveryCode.setText("")
                    }
                }
            }
    }

}