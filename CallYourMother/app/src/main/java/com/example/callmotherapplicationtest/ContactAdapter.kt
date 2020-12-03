package com.example.callmotherapplicationtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ContactAdapter(private val mContext : Context) : BaseAdapter() {

    private val mItems = ArrayList<ContactDetails>()
    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    internal class ViewHolder{
        var image : ImageView? = null
        var name : TextView? = null
        var status : TextView? = null
    }

    fun add (){
        TODO("Implement")
    }

    companion object{
        private var inflater: LayoutInflater? = null
    }
    init{
        inflater = LayoutInflater.from(mContext)
    }
}