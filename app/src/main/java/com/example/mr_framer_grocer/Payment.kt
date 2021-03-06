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
import com.example.mr_framer_grocer.databinding.ActivityPaymentBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class Payment : AppCompatActivity() {
    // Binding Payment Activity
    private lateinit var binding: ActivityPaymentBinding
    // For OTP purpose
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()   // For OTP purpose

        // Number Picker for Month
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = 12
        binding.monthPicker.wrapSelectorWheel = false

        // Number Picker for Year
        binding.yearPicker.minValue = 2021
        binding.yearPicker.maxValue = 2050
        binding.yearPicker.wrapSelectorWheel = false

        // Up button to My Cart Activity
        val back = binding.backBtn
        back.setOnClickListener {
            intent = Intent(this, MyCartActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        // Retrieving value using intent from Delivery Activity
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

        // Declaring variables for validation purpose
        val rb_credit_card = binding.radioCreditCard
        val rb_cod = binding.radioCod

        // Retrieve phone number using intent from Delivery Activity
        // Standardise phone number format
        // Send verification code to the phone if the phone number field is not empty
        binding.sendOtp.setOnClickListener {
            val intent = intent
            val phoneno = intent.getStringExtra("Phone_No").toString()
            val phoneNo = "+6$phoneno"
            binding.otpTxt.isEnabled = true     // Enable OTP edittext if user select "Send OTP" button


            if (!phoneNo.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Sending verification code",
                    Toast.LENGTH_LONG
                ).show()
                sendVerificationcode(phoneNo)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter phone number in your personal account. \n Settings -> Account ",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Validation before proceed to the next activity
        // Verify OTP code
        binding.proceedPaymentBtn.setOnClickListener {
            var code = binding.otpTxt.text.toString()
            if (rb_credit_card.isChecked) {
                if (binding.cardholderNameTxt.text.toString().isNotEmpty()) {
                    if (binding.cardNoTxt.text.toString().isNotEmpty()) {
                        if (binding.cardNoTxt.text.toString().length == 16) {
                            if (code.isNotEmpty()) {
                                verifyVerficationCode(code)
                            } else {
                                Toast.makeText(applicationContext,
                                    "Please enter the OTP sent to your device!",
                                    Toast.LENGTH_LONG).show()
                            }
                        } else {
                            binding.cardNoTxt.setError("Invalid credit card number!")
                            binding.cardNoTxt.setText("")
                        }
                    } else {
                        binding.cardNoTxt.setError("Required field!")
                    }
                } else {
                    binding.cardholderNameTxt.setError("Required field!")
                }
            } else if (rb_cod.isChecked) {


                // payment successful
                val bundle = intent.extras
                val method = bundle!!.getString("method")
                binding.progressBar.visibility = View.VISIBLE

                // if come from buy now
                if(method == "buynow")
                {
                    val stock = bundle.getInt("newstock")
                    val newstock = stock.toString()
                    val id = bundle.getString("prodID")
                    val cartItem = Common.cartRepository.getCartItems()

                    for(i in 0 until cartItem.size)
                    {
                        if(id!!.toInt() == cartItem[i].id)
                        {
                            Common.cartRepository.deleteCartItemById(id)
                        }

                    }
                    resetStockNum(id, newstock)
                    binding.progressBar.visibility = View.GONE
                }
                // if come from mycart, clear the cart after payment successful
                else
                {
                    val cartItem = Common.cartRepository.getCartItems()
                    for(i in 0 until cartItem.size)
                    {
                        resetStockNum(cartItem[i].id.toString(), (cartItem[i].stock-cartItem[i].quantity).toString())

                    }
                    binding.progressBar.visibility = View.GONE
                    Common.cartRepository.emptyCart()
                }

                val intent = Intent(applicationContext, OrderSuccessful::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext,
                    "Please choose one of the payment method!",
                    Toast.LENGTH_LONG).show()
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

                Toast.makeText(applicationContext, " Verification Completed!", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(applicationContext, "Authentication Failed!", Toast.LENGTH_LONG)
                    .show()
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

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(applicationContext,
                    "OTP code has been sent to your device!",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendVerificationcode(phoneNo: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNo) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)  // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallback
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
                    binding.progressBar.setVisibility(View.VISIBLE)

                    // payment successful
                    val bundle = intent.extras
                    val method = bundle!!.getString("method")
                    binding.progressBar.visibility = View.VISIBLE

                    // if come from buy now
                    if(method == "buynow")
                    {
                        val stock = bundle.getInt("newstock")
                        val newstock = stock.toString()
                        val id = bundle.getString("prodID")
                        val cartItem = Common.cartRepository.getCartItems()

                        for(i in 0 until cartItem.size)
                        {
                            if(id!!.toInt() == cartItem[i].id)
                            {
                                Common.cartRepository.deleteCartItemById(id)
                            }

                        }

                        resetStockNum(id, newstock)
                        binding.progressBar.visibility = View.GONE
                    }
                    // if come from mycart, clear the cart after payment successful
                    else
                    {
                        val cartItem = Common.cartRepository.getCartItems()
                        for(i in 0 until cartItem.size)
                        {
                            resetStockNum(cartItem[i].id.toString(), (cartItem[i].stock-cartItem[i].quantity).toString())

                        }
                        binding.progressBar.visibility = View.GONE
                        Common.cartRepository.emptyCart()
                    }


                    val intent = Intent(applicationContext, PaymentSuccessful::class.java)
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

    private fun resetStockNum(id: String?, newstock: String?) {

            val url = EndPoints.URL_UPDATESTOCK + "?stock=" + newstock + "&id=" + id

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    // Process the JSON
                    try{
                        if(response != null){
                            val strResponse = response.toString()
                            val jsonResponse  = JSONObject(strResponse)
                            val success: String = jsonResponse.get("success").toString()

                            Log.d("TAG","Update successful!")
                        }
                    }catch (e:Exception){
                        Log.d("Main", "Response: %s".format(e.message.toString()))


                    }
                },
                { error ->
                    Log.d("Main", "Response: %s".format(error.message.toString()))

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

    // Enable the layout if user select credit card for the payment method
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