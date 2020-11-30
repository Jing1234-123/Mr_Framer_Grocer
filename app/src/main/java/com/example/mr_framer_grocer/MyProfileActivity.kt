package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mr_framer_grocer.databinding.ActivityMyProfileBinding

class MyProfileActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var spinner:Spinner ? = null
    private var arrayAdapter:ArrayAdapter<String> ? = null

    private  var itemList = arrayOf("Male", "Female")
    private lateinit var binding: ActivityMyProfileBinding

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    //val birthDate = findViewById<DatePicker>(R.id.datePicker_birthDate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = findViewById(R.id.genderSpinner)
        arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, itemList)
        spinner?.adapter = arrayAdapter
        spinner?.onItemSelectedListener = this

        //get data from intent
        var intent = intent
        var phoneNo = intent.getStringExtra("Phone")

        //textView
        var phoneNum = findViewById<TextView>(R.id.contact)
        //setText
        phoneNum.text = phoneNo

        binding.backArrow.setOnClickListener {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.saveButton.setOnClickListener {
            if (binding.editTextName.text.toString().isNotEmpty()){
               /* if(binding.editTextPhone.text.toString().isNotEmpty()){*/
                   /* if(binding.editTextPhone.text.toString().length<10){*/
                if(binding.editTextEmail.text.toString().matches(emailPattern.toRegex())) {


                    if (binding.editTextAddress.text.toString().isNotEmpty()) {
                        intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please fill in your address",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext, "Invalid email address", Toast.LENGTH_LONG).show()
                }
                    /*}
                    else{
                        Toast.makeText(applicationContext, "Please enter a valid contact number", Toast.LENGTH_LONG).show()
                    }*/
                /* }
                 else{
                     Toast.makeText(applicationContext, "Please fill in your contact number", Toast.LENGTH_LONG).show()
                 }*/
            }
            else{
                Toast.makeText(applicationContext, "Please fill in your name", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(applicationContext, "Nothing Selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
         var items:String = parent?.getItemAtPosition(position)as String
        Toast.makeText(applicationContext, "$items", Toast.LENGTH_LONG).show()
    }
}