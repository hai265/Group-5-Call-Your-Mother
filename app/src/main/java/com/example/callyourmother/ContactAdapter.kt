package com.example.callmotherapplicationtest

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.callyourmother.AlarmNotificationReceiver
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(private val mContext : Context) : BaseAdapter() {

    private val mContacts = ArrayList<ContactDetails>()
    private val mAlarmIntents = ArrayList<PendingIntent>()
    private var inflater: LayoutInflater = LayoutInflater.from(mContext)
    val TAG = "Group-5-Call-Your-Mother"


    fun add (contact: ContactDetails){
        mContacts.add(contact)
        notifyDataSetChanged()

        //Create intent to broadcast to AlarmNotificationReciever


    }

    fun remove (position : Int){
        mContacts.removeAt(position)

        notifyDataSetChanged()
    }


    override fun getCount(): Int {
        return mContacts.size
    }

    override fun getItem(position: Int): ContactDetails {
        return mContacts[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var current = mContacts[position]
        val newView: View

        val viewHolder: ViewHolder

        if(null == convertView){

            viewHolder = ViewHolder()
            newView = inflater.inflate(R.layout.contact_item,parent,false)
            viewHolder.mContactLayout = newView.findViewById(R.id.contactItemLayout)
            viewHolder.nameView = newView.findViewById(R.id.contactName)
            //viewHolder.imageView = newView.findViewById(R.id.contactImage)
            viewHolder.daysView = newView.findViewById(R.id.daysLastCall)
            viewHolder.statusView = newView.findViewById(R.id.callStatus)
            viewHolder.position = position

            newView.tag = viewHolder
        }
        else
            viewHolder = convertView.tag as ViewHolder

        var cLastCalled = ContactDetails.FORMAT.format(current.lastCalled)


        viewHolder.nameView?.text = current.name
        viewHolder.daysView?.text = cLastCalled.substring(0,10)

        if(current.isLate)
            viewHolder.statusView?.text ="Call Now"
        else
            viewHolder.statusView?.text ="No need to call"


        return viewHolder.mContactLayout
    }

    internal class ViewHolder{
        var position: Int = 0
        var mContactLayout: ConstraintLayout? = null
    //    var imageView : ImageView? = null
        var nameView : TextView? = null
        var statusView : TextView? = null
        var daysView: TextView? = null
    }
}