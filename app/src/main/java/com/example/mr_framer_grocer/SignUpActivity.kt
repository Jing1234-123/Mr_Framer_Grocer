package com.example.mr_framer_grocer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        auth = FirebaseAuth.getInstance()

        /*login()*/
        binding.getOTPBtn.setOnClickListener {
            /*val intent = intent*/
            var phoneno = findViewById<TextView>(R.id.editTextPhone).text.toString().trim()
            var phoneNo = "+6$phoneno"

            if (editTextPhone.text.toString().isNotEmpty()) {
                Toast.makeText(applicationContext, "OTP button has been pressed", Toast.LENGTH_LONG).show()
                editTextPhone.isEnabled = false
                sendVerificationcode(phoneNo)
            } else {
                editTextPhone.setError("Phone Number Empty")
            }
        }

        binding.signUpButton.setOnClickListener {
            var code = binding.editTextOTP.text.toString()

            if (code.isNotEmpty()) {
                verifyVerficationCode(code)
            } else {
                editTextOTP.setError("Please enter the OTP sent to your device")
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
                editTextPhone.isEnabled = true
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
                binding.editTextOTP.isEnabled = true
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
                        // if password not empty
                        if (binding.editTextTextPassword.text.toString().isNotEmpty()){
                            //check psw length
                            if (binding.editTextTextPassword.text.toString().length > 5){
                                // chk psw and confirm psw
                                if (binding.editTextTextPasswordConfirm.text.toString() == binding.editTextTextPassword.text.toString()){
                                    // chk whether user exist
                                    verifyUser()
                                }
                                // two psw not match
                                else{
                                    //Toast.makeText(applicationContext, "Password and Confirm Password does not match", Toast.LENGTH_LONG).show()
                                    editTextTextPasswordConfirm.setError("Password and Confirm Password does not match")
                                }
                            }
                            // invalid psw length
                            else{
                                //Toast.makeText(applicationContext, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
                                editTextTextPassword.setError("Password must be at least 6 characters")
                            }
                        }
                        // if psw empty
                        else{
                            editTextTextPassword.setError("Password Empty")
                            //Toast.makeText(applicationContext, "Please enter your password (6 to 12 characters)", Toast.LENGTH_LONG).show()
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

    private fun verifyUser() {
        binding.progress!!.visibility = View.VISIBLE

        val jsonObjectRequest = StringRequest(
            Request.Method.GET, EndPoints.URL_VERIFY_USER + "?contact_no=" + binding.editTextPhone.text.toString(),
            Response.Listener{ response ->
                try {
                    if (response != null) {
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                        // user exist
                        Toast.makeText(applicationContext, "User already exist!", Toast.LENGTH_LONG).show()
                        editTextOTP.text.clear()
                        binding.editTextPhone.isEnabled = true
                        binding.editTextOTP.isEnabled = false
                        editTextTextPassword.text.clear()
                        editTextTextPasswordConfirm.text.clear()

                    } else {
                        binding.progress!!.visibility = View.GONE

                    }
                    binding.progress!!.visibility = View.GONE

                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.progress!!.visibility = View.GONE
                }

                // user do not exist in database, can sign in-
            }, Response.ErrorListener { volleyError ->
                binding.progress!!.visibility = View.GONE
                // Sign in success, update UI with the signed-in user's information
                Common.contact_no = binding.editTextPhone.text.toString()
                Common.psw = binding.editTextTextPassword.text.toString()

                val intent = Intent(applicationContext, MyProfileActivity::class.java)
                intent.putExtra("edit_profile", "no")
                finish()
                startActivity(intent)
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
}