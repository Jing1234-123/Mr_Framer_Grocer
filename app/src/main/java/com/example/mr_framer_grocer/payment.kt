package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityPaymentBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class payment : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var et_cardholder_name:EditText
    lateinit var et_card_num:EditText
    lateinit var otp_btn:Button

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

        val back = binding.backBtn
        back.setOnClickListener {
            intent = Intent(this, Delivery::class.java)
            startActivity(intent)
        }

        val intent = intent
        val subtotal = intent.getStringExtra("Subtotal").toString()
        val delivery_fee = intent.getStringExtra("Delivery_Fee").toString()
        val total = intent.getStringExtra("Total").toString()

        val new_subtotal = binding.subtotalTxt
        new_subtotal.text = subtotal
        val new_delivery_fee = binding.deliveryFeeTxt
        new_delivery_fee.text = delivery_fee
        val new_total = binding.totalTxt
        new_total.text = total

        var rb_credit_card = binding.radioCreditCard
        var rb_cod = binding.radioCod

//        et_cardholder_name = binding.cardholderNameTxt
//        et_card_num = binding.cardNoTxt

//        if(et_card_num.text.trim().isNotEmpty() && et_cardholder_name.text.trim().isNotEmpty()){
//
//            //binding.sendOtp.isEnabled = true
//
//            binding.sendOtp.setOnClickListener {
//                val intent = intent
//                val phoneno = intent.getStringExtra("Phone_No").toString()
//                var phoneNo = "+6$phoneno"
//                binding.otpTxt.isEnabled = true
//
//                if (!phoneNo.isEmpty()) {
//                    sendVerificationcode(phoneNo)
//                } else {
//                    Toast.makeText(
//                        applicationContext,
//                        "Please enter phone number in your personal account. \n Settings -> Account ",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//        else if (et_card_num.text.trim().isNotEmpty() || et_cardholder_name.text.trim().isNotEmpty()){
//            Toast.makeText(
//                applicationContext,
//                "Please enter credit card details! ",
//                Toast.LENGTH_LONG
//            ).show()
//        }

//        val textWatcher = object :TextWatcher{
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//
//            override fun afterTextChanged(editable: Editable?) {
//                if (editable != null && !editable.toString().equals("")) {
//                    // Checking editable.hashCode() to understand which edittext is using right now
//                    if (et_cardholder_name.hashCode() === editable.hashCode()) {
//                        // This is just an example, your magic will be here!
////                        val value = editable.toString()
////                        et_cardholder_name!!.removeTextChangedListener(this)
////                        et_cardholder_name!!.setText(value)
////                        et_cardholder_name!!.addTextChangedListener(this)
//                        binding.sendOtp.isEnabled = true
//                    }
//                } else if (et_card_num.hashCode() === editable!!.hashCode()) {
//                    // This is just an example, your magic will be here!
////                    val value = editable!!.toString()
////                    et_card_num!!.removeTextChangedListener(this)
////                    et_card_num!!.setText(value)
////                    et_card_num!!.addTextChangedListener(this)
//                    binding.sendOtp.isEnabled = true
//                }
//            }
//        }

        binding.sendOtp.setOnClickListener {
            val intent = intent
            val phoneno = intent.getStringExtra("Phone_No").toString()
            var phoneNo = "+6$phoneno"
            binding.otpTxt.isEnabled = true

            if (!phoneNo.isEmpty()) {
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
            if(rb_credit_card.isChecked){
                var code = binding.otpTxt.text.toString()
                if (code.isNotEmpty()) {
                    verifyVerficationCode(code)
                } else {
                    Toast.makeText(applicationContext,
                        "Please enter the OTP sent to your device.",
                        Toast.LENGTH_LONG).show()
                }
            }else if(rb_cod.isChecked){
                val intent = Intent(applicationContext, OrderSuccessful::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, "Please choose one of the payment method!", Toast.LENGTH_LONG).show()
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

    private fun sendVerificationcode(phoneNo: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNo) // Phone number to verify
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
            binding.creditCardDetails.setVisibility(View.VISIBLE)
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
            binding.creditCardDetails.setVisibility(View.INVISIBLE)
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







