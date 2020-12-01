package com.example.mr_framer_grocer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mr_framer_grocer.Model.ProfileModel
import com.example.mr_framer_grocer.R

class ProfileListAdapter (var mCtx: Context, var resource:Int, var items:List<ProfileModel>)
    : ArrayAdapter<ProfileModel>(mCtx, resource, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //inflate layout
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(resource, null)
        val imageView: ImageView = view.findViewById(R.id.profile_item_icon)
        val textView: TextView = view.findViewById(R.id.profile_item_name)

        var mItems:ProfileModel = items[position]

        imageView.setImageDrawable(mCtx.resources.getDrawable(mItems.photo))
        textView.text = mItems.item_name
        return view
    }

}