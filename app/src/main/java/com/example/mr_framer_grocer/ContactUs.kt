package com.example.mr_framer_grocer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mr_framer_grocer.databinding.ActivityContactUsBinding

class ContactUs : AppCompatActivity() {
    // Binding Contact Us Activity
    private lateinit var binding: ActivityContactUsBinding

    // For phone call purpose
    val mobno = "0172073921"
    val REQUEST_PHONE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For phone call purpose
        val call = binding.mobileDetails
        call.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL)
            }
            else{
                makePhoneCall()
            }
        }

        // For sending email purpose using intent
        val email = binding.emailDetails
        email.setOnClickListener{
            val recipient = "hello@mrfarmergroup.com"
            val subject = "Enquiries on Mr Farmer Grocer"

            sendEmail(recipient, subject)
        }

        // For viewing street view purpose using intent
        val openMap = binding.addressDetails
        openMap!!.setOnClickListener {
            // Mr Farmer Grocer - Latitude, Longitude
            // 3.089613,101.691924

            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("google.streetview:cbll=3.089613,101.691924")

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")

            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }

        // For opening Facebook purpose using intent
        val openFB = binding.facebookDetails
        openFB.setOnClickListener {
            val fb_url = "https://www.facebook.com/mrfarmergrocer/"
            val fbIntent = Intent(Intent.ACTION_VIEW)
            fbIntent.data = Uri.parse(fb_url)
            startActivity(fbIntent)
        }

        // For viewing official website of Mr Farmer Grocer purpose using intent
        val openWeb = binding.websiteDetails
        openWeb.setOnClickListener {
            val url = "https://www.mrfarmergrocer.com"
            val urlIntent = Intent(Intent.ACTION_VIEW)
            urlIntent.data = Uri.parse(url)
            startActivity(urlIntent)
        }

        // Up button to My Profile Activity
        val back = binding.backBtn
        back.setOnClickListener{
            intent=Intent(this, MyProfileActivity::class.java)  /////////////////------------->>>>>>>>>>>>>>>> HERE
            startActivity(intent)
        }
    }

    // Sending email function using intent
    private fun sendEmail(recipient: String, subject: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // Put recipient email in intent
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        // Put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        try {
            // Start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            // If any thing goes wrong for example no email client application or any exception get and show exception message
            Toast.makeText(this, "There is no email client in this device!", Toast.LENGTH_LONG).show()
        }
    }

    // Phone call function using intent
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==REQUEST_PHONE_CALL){
            makePhoneCall()
        }
    }

    private fun makePhoneCall(){
        val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobno, null))
        startActivity(intent)

    }
}