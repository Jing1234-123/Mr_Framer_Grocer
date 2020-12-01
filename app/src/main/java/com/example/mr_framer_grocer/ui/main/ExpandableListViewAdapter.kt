package com.example.mr_framer_grocer.ui.main

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.mr_framer_grocer.R

class ExpandableListViewAdapter internal constructor(
    private val context:Context,
    private val expandable_list: List<String>,
    private val expandable_list_text: HashMap<String, List<String>>
): BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.expandable_list_text[this.expandable_list[listPosition]]!![expandedListPosition]
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.expandable_list_text, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.expandable_text)
        expandedListTextView.text = expandedListText
        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandable_list_text[this.expandable_list[listPosition]]!!.size
    }
    override fun getGroup(listPosition: Int): Any {
        return this.expandable_list[listPosition]
    }
    override fun getGroupCount(): Int {
        return this.expandable_list.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.expandable_list, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.expandable_title)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}