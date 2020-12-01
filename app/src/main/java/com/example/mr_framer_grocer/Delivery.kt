package com.example.mr_framer_grocer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_framer_grocer.databinding.ActivityDeliveryBinding
import com.example.mr_framer_grocer.databinding.ActivityPaymentBinding

class Delivery : AppCompatActivity() {
    // Binding Delivery Activity
    private lateinit var binding: ActivityDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showEditAddDialog()

        // Up button to Previous Activity
        val back = binding.backBtn
        back.setOnClickListener {
            intent = Intent(this, TestingCheckout::class.java)
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
                val subtotal = binding.subtotalTxt.text.toString()
                val delivery_fee = binding.deliveryFeeTxt.text.toString()
                val total = binding.totalTxt.text.toString()
                val intent = Intent(this@Delivery, payment::class.java)
                intent.putExtra("Phone_No", phoneNo)
                intent.putExtra("Subtotal", subtotal)
                intent.putExtra("Delivery_Fee", delivery_fee)
                intent.putExtra("Total", total)
                startActivity(intent)
            } else if (rb_self_pickup.isChecked) {
                val phoneNo = binding.custPhoneNumber.text.toString()
                val subtotal = binding.subtotalTxt.text.toString()
                val delivery_fee = binding.deliveryFeeTxt.text.toString()
                val total = binding.totalTxt.text.toString()
                val intent = Intent(this@Delivery, payment::class.java)
                intent.putExtra("Phone_No", phoneNo)
                intent.putExtra("Subtotal", subtotal)
                intent.putExtra("Delivery_Fee", delivery_fee)
                intent.putExtra("Total", total)
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
        var subtotal = binding.subtotalTxt.text.toString()
        var subttl: Float = subtotal.toFloat()
        var total = binding.totalTxt.text.toString()
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
                var delivery_fee = binding.deliveryFeeTxt.text.toString()
                var deliver_fee: Float = delivery_fee.toFloat()
                ttl = subttl + deliver_fee
                binding.totalTxt.text = "%.2f".format(ttl)
            } else if (position == 1) {
                binding.deliveryFeeTxt.text = "10.00"
                var delivery_fee = binding.deliveryFeeTxt.text.toString()
                var deliver_fee: Float = delivery_fee.toFloat()
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
        var subtotal = binding.subtotalTxt.text.toString()
        var subttl: Float = subtotal.toFloat()
        var total = binding.totalTxt.text.toString()
        var ttl: Float = total.toFloat()

        binding.deliveryFeeTxt.text = "0.00"
        var delivery_fee = binding.deliveryFeeTxt.text.toString()
        var deliver_fee: Float = delivery_fee.toFloat()
        ttl = subttl + deliver_fee
        binding.totalTxt.text = "%.2f".format(ttl)
    }

}