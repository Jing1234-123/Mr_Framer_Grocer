package com.example.mr_framer_grocer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.mr_framer_grocer.Database.EndPoints
import com.example.mr_framer_grocer.Database.MySingleton
import com.example.mr_framer_grocer.Model.User
import com.example.mr_framer_grocer.databinding.ActivityDeliveryBinding
import org.json.JSONException
import org.json.JSONObject

class Delivery : AppCompatActivity() {
    // Binding Delivery Activity
    private lateinit var binding: ActivityDeliveryBinding
    private var subtotal: String? = null
    lateinit var userInfo: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showEditAddDialog()
//
        val bundle = intent.extras

        subtotal = bundle!!.getFloat("subtotal").toString()
       val method = bundle.getString("method")


        // user infor
        getUserInfo()
        binding.customerName.text = userInfo.name
        binding.customerAddress.text = userInfo.address
        binding.custPhoneNumber.text = userInfo.contact

        // Up button to Previous Activity
        val back = binding.backBtn
        back.setOnClickListener {
            intent = Intent(this, AllCategory::class.java) /////////////////------------->>>>>>>>>>>>>>>> HERE
            startActivity(intent)
        }

        // Declaring variables for validation purpose
        val rb_door_delivery = binding.radioDoorDelivery
        val rb_self_pickup = binding.radioSelfPickup

        // Validation before proceed to the next activity
        // Passing values to next activity using intent
        binding.proceedBtn.setOnClickListener {
            if (rb_door_delivery.isChecked) {
                val phoneNo = binding.custPhoneNumber.text.toString()
                subtotal = binding.subtotalTxt.text.toString()
                val delivery_fee = binding.deliveryFeeTxt.text.toString()
                val total = binding.totalTxt.text.toString()
                val intent = Intent(this@Delivery, Payment::class.java)
                intent.putExtra("Phone_No", phoneNo)
                intent.putExtra("Subtotal", subtotal)
                intent.putExtra("Delivery_Fee", delivery_fee)
                intent.putExtra("Total", total)
                intent.putExtra("method", method)
                if(method == "buynow")
                {
                    intent.putExtra("prodID", bundle.getString("prodID"))
                }
                startActivity(intent)
            } else if (rb_self_pickup.isChecked) {
                val phoneNo = binding.custPhoneNumber.text.toString()
                val subtotal = binding.subtotalTxt.text.toString()
                val delivery_fee = binding.deliveryFeeTxt.text.toString()
                val total = binding.totalTxt.text.toString()
                val intent = Intent(this@Delivery, Payment::class.java)
                intent.putExtra("Phone_No", phoneNo)
                intent.putExtra("Subtotal", subtotal)
                intent.putExtra("Delivery_Fee", delivery_fee)
                intent.putExtra("Total", total)
                intent.putExtra("method", method)
                if(method == "buynow")
                {
                    intent.putExtra("prodID", bundle.getString("prodID"))
                }
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,
                    "Please choose one of the delivery method!",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    // Show layout if user request to change address
    private fun showEditAddDialog() {
        binding.changeAddInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_add_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.edit_add)

            with(builder) {
                setTitle("Change Address Details: ")
                setPositiveButton("Done") { dialog, which ->
                    if (editText.text.trim().length > 0) {
                        binding.customerAddress.text = editText.text.toString()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No new address has been modified! ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                setNegativeButton("Cancel") { dialog, which ->
                    Toast.makeText(
                        applicationContext,
                        "No new address has been modified! ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                setView(dialogLayout)
                show()
            }
        }
    }

    // Prompt dialog if user select "Door Delivery" as delivery method
    fun onDoorDeliveryClicked(view: View) {
        val subtotal = binding.subtotalTxt.text.toString()
        val subttl: Float = subtotal.toFloat()
        val total = binding.totalTxt.text.toString()
        var ttl: Float = total.toFloat()

        // Alert dialog for door delivery
        val options = arrayOf(
            "Delivery to Peninsular Malaysia \n RM 5.00",
            "Deliver to Sabah/Sarawak \n RM 10.00"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please note that:")

        // Update delivery fees and re-calculate the total
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            val position = (dialog as AlertDialog).listView.checkedItemPosition
            if (position == 0) {
                binding.deliveryFeeTxt.text = "5.00"
                val delivery_fee = binding.deliveryFeeTxt.text.toString()
                val deliver_fee: Float = delivery_fee.toFloat()
                ttl = subttl + deliver_fee
                binding.totalTxt.text = "%.2f".format(ttl)
            } else if (position == 1) {
                binding.deliveryFeeTxt.text = "10.00"
                val delivery_fee = binding.deliveryFeeTxt.text.toString()
                val deliver_fee: Float = delivery_fee.toFloat()
                ttl = subttl + deliver_fee
                binding.totalTxt.text = "%.2f".format(ttl)
            }
            dialog.dismiss()
        }

        val delivery_dialog = builder.create()
        delivery_dialog.setCanceledOnTouchOutside(false)
        delivery_dialog.show()
    }

    // Make sure no delivery charges will be apply if user request for self-pickup
    fun onSelfPickupClicked(view: View) {
        val subtotal = binding.subtotalTxt.text.toString()
        val subttl: Float = subtotal.toFloat()
        val total = binding.totalTxt.text.toString()
        var ttl: Float = total.toFloat()

        binding.deliveryFeeTxt.text = "0.00"
        val delivery_fee = binding.deliveryFeeTxt.text.toString()
        val deliver_fee: Float = delivery_fee.toFloat()
        ttl = subttl + deliver_fee
        binding.totalTxt.text = "%.2f".format(ttl)
    }

    fun getUserInfo(){
        var exist = false
        //verify user in database
        binding.progress!!.visibility = View.VISIBLE
        val jsonObjectRequest = StringRequest(
            Request.Method.GET, EndPoints.URL_VERIFY_USER + "?contact_no=" + Common.contact_no,
            Response.Listener{ response ->
                try {
                    if (response != null) {
                        // get data in JSON format
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)

                        userInfo = User(
                            jsonResponse.getString("name"),
                            jsonResponse.getString("gender"),
                            jsonResponse.getString("birth_date"),
                            jsonResponse.getString("contact_no"),
                            jsonResponse.getString("email"),
                            jsonResponse.getString("address"),
                            jsonResponse.getString("password")
                        )

                        exist = true

                    } else {
                        binding.progress!!.visibility = View.GONE
                        Toast.makeText(applicationContext, "User not exist", Toast.LENGTH_LONG).show()

                    }

                    binding.progress!!.visibility = View.GONE

                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.progress!!.visibility = View.GONE
                    Toast.makeText(applicationContext, "Connection Lost!", Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener { volleyError ->
                binding.progress!!.visibility = View.GONE
                Toast.makeText(applicationContext, "User not exist", Toast.LENGTH_LONG).show() })

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