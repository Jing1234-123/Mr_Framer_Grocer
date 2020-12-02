package com.example.mr_framer_grocer

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Model.User
import com.example.mr_framer_grocer.databinding.ActivityMyProfileBinding
import kotlinx.android.synthetic.main.activity_my_profile.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MyProfileActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var spinner:Spinner ? = null
    private var arrayAdapter:ArrayAdapter<String> ? = null

    private lateinit var newUser: User
    private  var itemList = arrayOf("Male", "Female")
    private lateinit var binding: ActivityMyProfileBinding
    var items: String? = null


    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set phone number
        binding.contact.text = Common.contact_no

        spinner = binding.genderSpinner
        arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, itemList)
        spinner?.adapter = arrayAdapter
        spinner?.onItemSelectedListener = this

        // set date Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
        val day = c.get(Calendar.DAY_OF_MONTH)


        //show day picker dialog on click
        select_birthDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1//mMonth += 1
                birthDate.setText(""+ mDay +"/"+ mmMonth +"/"+ mYear)
            }, year, month, day)

            dpd.datePicker.maxDate = c.getTimeInMillis()
            dpd.show()
        }

        val bundle = intent.extras
        if(bundle!!.getString("edit_profile") == "yes")
        {
            getUserInfo()
        }

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.saveButton.setOnClickListener {
            if (binding.editTextName.text.toString().isNotEmpty()){

                if(binding.editTextEmail.text.toString().matches(emailPattern.toRegex())) {

                    if (binding.editTextAddress.text.toString().isNotEmpty()) {

                        // user edit profile
                        if(bundle.getString("edit_profile") == "yes")
                        {
                            updateUserInfo()
                        }

                        //user create new profile
                        if(bundle.getString("edit_profile") == "no"){
                            // insert user data
                            createUser()
                            val intent = Intent(this, ProfileActivity::class.java)
                            finish()
                            startActivity(intent)
                        }

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

            }
            else{
                Toast.makeText(applicationContext, "Please fill in your name", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUserInfo() {

        val url = EndPoints.URL_UPDATE_USER + "?name=" + binding.editTextName.text.toString() +
                "&gender=" + items + "&birth_date=" + binding.birthDate.text.toString() +
                "&contact_no=" + Common.contact_no + "&email=" + binding.editTextEmail.text.toString()+
                "&address=" + binding.editTextAddress.text.toString()+ "&password=" + Common.psw

        binding.progress.visibility = View.VISIBLE

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val success: String = jsonResponse.get("success").toString()

                        if(success.equals("1")){
                            Toast.makeText(applicationContext, "Updated successfully!", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(applicationContext, "Fail to update", Toast.LENGTH_LONG).show()
                        }
                        binding.progress.visibility = View.GONE
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))
                    binding.progress.visibility = View.GONE

                }
            },
            { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
                binding.progress.visibility = View.GONE
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

    private fun getUserInfo() {
        binding.progress.visibility = View.VISIBLE
        val jsonObjectRequest = StringRequest(
            Request.Method.GET, EndPoints.URL_VERIFY_USER + "?contact_no=" + Common.contact_no,
            Response.Listener{ response ->
                try {
                    if (response != null) {
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                        binding.editTextName.setText(jsonResponse.getString("name"), TextView.BufferType.EDITABLE)
                        binding.birthDate.setText(jsonResponse.getString("birth_date"), TextView.BufferType.EDITABLE)
                        binding.contact.setText(jsonResponse.getString("contact_no"), TextView.BufferType.EDITABLE)
                        binding.editTextEmail.setText(jsonResponse.getString("email"), TextView.BufferType.EDITABLE)
                        binding.editTextAddress.setText(jsonResponse.getString("address"), TextView.BufferType.EDITABLE)

                        if(jsonResponse.getString("gender") == "Female")
                        {
                            itemList = arrayOf("Female", "Male")
                            arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, itemList)
                            spinner?.adapter = arrayAdapter
                            spinner?.onItemSelectedListener = this
                        }

                    } else {
                        binding.progress.visibility = View.GONE

                    }
                    binding.progress.visibility = View.GONE

                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.progress.visibility = View.GONE
                }

                // user do not exist in database, can sign in-
            }, Response.ErrorListener { volleyError ->
                binding.progress.visibility = View.GONE
                Toast.makeText(applicationContext, "User not exist!", Toast.LENGTH_LONG).show()
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

    private fun createUser() {

        val url = EndPoints.URL_CREATE_USER + "?name=" + binding.editTextName.text.toString() +
                "&gender=" + items + "&birth_date=" + binding.birthDate.text.toString() +
                "&contact_no=" + Common.contact_no + "&email=" + binding.editTextEmail.text.toString()+
                "&address=" + binding.editTextAddress.text.toString()+ "&password=" + Common.psw

        binding.progress.visibility = View.VISIBLE

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val success: String = jsonResponse.get("success").toString()

                        if(success.equals("1")){
                            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(applicationContext, "Record not saved", Toast.LENGTH_LONG).show()
                        }
                        binding.progress.visibility = View.GONE
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))
                    binding.progress.visibility = View.GONE

                }
            },
            { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
                binding.progress.visibility = View.GONE
                Toast.makeText(applicationContext, "Record not saved", Toast.LENGTH_LONG).show()
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

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(applicationContext, "Nothing Selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        items= parent?.getItemAtPosition(position) as String
        //Toast.makeText(applicationContext, "$items", Toast.LENGTH_LONG).show()
    }


}