package com.zaid.midweektaskcalllog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import android.widget.BaseAdapter
import com.zaid.midweektaskcalllog.R.*
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter(val context: Context, val list: ArrayList<CallDetials>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false)
        view.list_item.text = "Caller name : ${list[position].name}\n" +
                "Phone number: ${list[position].number}\n" +
                "Call duration: ${list[position].duration}"
        return view
    }


}