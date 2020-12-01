package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityPaymentBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class payment : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = 12
        binding.monthPicker.wrapSelectorWheel = false

        binding.yearPicker.minValue = 2021
        binding.yearPicker.maxValue = 2050
        binding.yearPicker.wrapSelectorWheel = false

        binding.sendOtp.setOnClickListener {
           // val mobileNumber = findViewById<TextView>(R.id.cust_phone_number)
           // var phoneno = mobileNumber.text.toString().trim()
            // (2) var phoneno = findViewById<TextView>(R.id.cust_phone_number).text.toString().trim()
            // The test phone number and code should be whitelisted in the console.
          //  var phoneno = "0142468151".trim()

            val phoneNo = "+60164666826"

            if (!phoneNo.isEmpty()) {
             //   phoneno = "+6" + phoneno
                sendVerificationcode(phoneNo)
            } else {
                Toast.makeText(
                        applicationContext,
                        "Please enter phone number in your personal account. \n Settings -> Account ",
                        Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.proceedPaymentBtn.setOnClickListener {
            var code = binding.otpTxt.toString()

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

//                val code = credential.smsCode
//                if (code != null) {
//                    binding.otpTxt.setText(code)
//                    verifyVerficationCode(code)
//                }
//                else{
//                    payment(credential)
//                }

                Toast.makeText(applicationContext, " Congratssss", Toast.LENGTH_LONG)
                        .show()

              //  startActivity(Intent(applicationContext, payment_successful::class.java))
             //   finish()
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
               // var intent = Intent(applicationContext,payment_successful::class.java)  // not sure this correct ma
               // intent.putExtra("storedVerificationId",storedVerificationId)
               // startActivity(intent) //not sure this correct maaaa
                Toast.makeText(applicationContext, "HELOOO!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun sendVerificationcode(phoneNo: String) {
       // val smsCode = "123456"

        val firebaseAuthSettings = auth.firebaseAuthSettings

        // Configure faking the auto-retrieval with the whitelisted numbers.
    //    firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNo, smsCode)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNo) //Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyVerficationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        payment(credential)
    }

    private fun payment(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                //    val user = task.result?.user
                    Toast.makeText(applicationContext, "Payment successfully!", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(applicationContext, payment_successful::class.java)
                    startActivity(intent)
                    finish()


                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(
                                applicationContext,
                                "Payment failed with invalid OTP!",
                                Toast.LENGTH_LONG
                        ).show()
                        binding.otpTxt.setText("")
                    }
                }
            }
    }

    fun showLayout(view: View) {

        if (binding.radioCreditCard.isChecked) {
            binding.cardholderNameInfo.setVisibility(View.VISIBLE)
            binding.cardholderNameTxt.setVisibility(View.VISIBLE)
            binding.cardNoInfo.setVisibility(View.VISIBLE)
            binding.cardNoTxt.setVisibility(View.VISIBLE)
            binding.expiryDateInfo.setVisibility(View.VISIBLE)
            binding.monthPicker.setVisibility(View.VISIBLE)
            binding.yearPicker.setVisibility(View.VISIBLE)
            binding.otpInfo.setVisibility(View.VISIBLE)
            binding.otpTxt.setVisibility(View.VISIBLE)
            binding.sendOtp.setVisibility(View.VISIBLE)
        } else {
            binding.cardholderNameInfo.setVisibility(View.INVISIBLE)
            binding.cardholderNameTxt.setVisibility(View.INVISIBLE)
            binding.cardNoInfo.setVisibility(View.INVISIBLE)
            binding.cardNoTxt.setVisibility(View.INVISIBLE)
            binding.expiryDateInfo.setVisibility(View.INVISIBLE)
            binding.monthPicker.setVisibility(View.INVISIBLE)
            binding.yearPicker.setVisibility(View.INVISIBLE)
            binding.otpInfo.setVisibility(View.INVISIBLE)
            binding.otpTxt.setVisibility(View.INVISIBLE)
            binding.sendOtp.setVisibility(View.INVISIBLE)
        }
    }


}






