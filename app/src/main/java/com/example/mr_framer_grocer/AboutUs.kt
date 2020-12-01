package com.example.mr_framer_grocer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mr_framer_grocer.databinding.ActivityAboutUsBinding
import com.example.mr_framer_grocer.ui.main.ExpandableListViewAdapter

class AboutUs : AppCompatActivity() {
    // Binding About Us Activity
    private lateinit var binding: ActivityAboutUsBinding

    // For expandable listview purpose
    private lateinit var listViewAdapter: ExpandableListViewAdapter
    private lateinit var expandable_list:List<String>
    private lateinit var expandable_list_text:HashMap<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Up button to My Profile Activity
        val back = binding.backBtn
        back.setOnClickListener{
            intent= Intent(this,MyProfileActivity::class.java)  /////////////////------------->>>>>>>>>>>>>>>> HERE
            startActivity(intent)
        }

        // Expandable listview
        showList()
        listViewAdapter = ExpandableListViewAdapter(this,expandable_list,expandable_list_text)
        binding.eListView.setAdapter(listViewAdapter)
    }

    // Expandable Listview
    private fun showList(){
        expandable_list = ArrayList()
        expandable_list_text = HashMap()

        (expandable_list as ArrayList<String>).add("Our Vision")
        (expandable_list as ArrayList<String>).add("Our Mission")
        (expandable_list as ArrayList<String>).add("Core Value")
        (expandable_list as ArrayList<String>).add("What We Provide?")

        val vision:MutableList<String> = ArrayList()
        vision.add("To be leading fresh supply chain platform in ASEAN.\n")

        val mission:MutableList<String> = ArrayList()
        mission.add("To improve farmer’s benefited, and provide quality fresh products by collaborating and completing the fresh supply chain.\n")

        val core_value:MutableList<String> = ArrayList()
        core_value.add("Quality Assurance")
        core_value.add("Reasonable Price")
        core_value.add("Efficient Delivery")
        core_value.add("Walk the Talk")
        core_value.add("Respect & Responsibility")
        core_value.add("Market Leading")
        core_value.add("Unity")
        core_value.add("Integrity")
        core_value.add("Clean & Comfortable Environment")
        core_value.add("Win & Win Situation")

        val provided:MutableList<String> = ArrayList()
        provided.add("Best Prices & Offers")
        provided.add("Easy Returns")
        provided.add("Easy & Next Day Delivery")

        expandable_list_text[expandable_list[0]]=vision
        expandable_list_text[expandable_list[1]]=mission
        expandable_list_text[expandable_list[2]]=core_value
        expandable_list_text[expandable_list[3]]=provided
    }
}