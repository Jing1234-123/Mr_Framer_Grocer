package com.example.mr_framer_grocer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mr_framer_grocer.databinding.ActivityContactUsBinding

class ContactUs : AppCompatActivity() {
    private lateinit var binding:ActivityContactUsBinding
    val mobno = "0172073921"
    val REQUEST_PHONE_CALL=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_contact_us)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        intent = Intent(this,ContactUs::class.java)
//        startActivity(intent)

        val call = binding.callBtn
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

        val email = binding.emailBtn
        email.setOnClickListener{
            val recipient = "hello@mrfarmergroup.com"
            val subject = "Enquiries on Mr Farmer Grocer"

            sendEmail(recipient, subject)
        }

        //3.0895823969157687, 101.6919338898916

        val openMap = binding.mapBtn
        openMap!!.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:3.089613,101.691924"))
//            startActivity(intent)

            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("google.streetview:cbll=3.089613,101.691924")

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")

            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }

        val back = binding.backBtn
        back.setOnClickListener{
            intent=Intent(this, TestingHomeScreen::class.java)
            startActivity(intent)
        }
    }

    private fun sendEmail(recipient: String, subject: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, "There is no email client in this device!", Toast.LENGTH_LONG).show()
        }
    }

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