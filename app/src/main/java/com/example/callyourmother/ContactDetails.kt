package com.example.callmotherapplicationtest

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log

import java.sql.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ContactDetails {
    var name : String? = null
    var phoneNumber : String? = null
    var frequency :Int? = null
    var lastCalled = Date()
    var isLate : Boolean = false
    var notificationIntent : PendingIntent? = null

    internal constructor(name: String, phoneNumber: String, lastCalled: Date, frequency : String){

        this.name = name
        this.phoneNumber = phoneNumber
        this.lastCalled = lastCalled
        this.frequency = frequency.toInt()

        checkIfLate()
    }

    internal constructor(intent: Intent){
        name = intent.getStringExtra(NAME)
        phoneNumber = intent.getStringExtra(PHONENUMBER)
        //pass in the time as a toString first
        try {
            lastCalled = FORMAT.parse(intent.getStringExtra(LASTCALLED))
        } catch (e: ParseException) {
            lastCalled = Date()
        }

        frequency = intent.getIntExtra(FREQUENCY,1)

        checkIfLate()

    }
    fun setNotificationPendingIntent(pendingIntent: PendingIntent){
        notificationIntent = pendingIntent
    }

    override fun toString(): String {
        return (name + ITEM_SEP + phoneNumber + ITEM_SEP + frequency + ITEM_SEP
                + FORMAT.format(lastCalled))
    }

    override fun equals(other: Any?): Boolean {
        if(other is ContactDetails)
            return name.equals(other.name) && phoneNumber.equals(other.phoneNumber)
        return false
    }

    fun updateLastCalled(update:Date){

        lastCalled = update
        checkIfLate()
    }

    fun updateFrequency(update:Int){

        frequency = update
        checkIfLate()
    }
    fun getUniqueID() : Int{
        val unformattedPhoneNumber = phoneNumber?.replace("\\D".toRegex(),"" )
        return if (unformattedPhoneNumber != null) {
            unformattedPhoneNumber.toLong().toInt()
        } else 0
    }

    //a private function to check if the user is late or not
    private fun checkIfLate(){

        var currentTime = Calendar.getInstance().getTime()
        var cLastCalled = Calendar.getInstance()
        cLastCalled.setTime(lastCalled)
        cLastCalled.add(Calendar.DATE, this.frequency!!)
        var lateDate = cLastCalled.getTime()

        isLate = currentTime.after(lateDate)
    }

    companion object{

        val ITEM_SEP = System.getProperty("line.separator")
        val NAME = "name"
        val PHONENUMBER = "phonenumber"
        val LASTCALLED = "lastCalled"
        val FREQUENCY = "frequency"

        val FORMAT = SimpleDateFormat(
            "EEE MMM d HH:mm:ss zzz yyyy", Locale.US)

        fun packageToIntent(name : String, phoneNumber: String, lastCalled: Date, frequency: Int) : Intent{

            val intent = Intent()
            intent.putExtra(NAME,name)
            intent.putExtra(PHONENUMBER,phoneNumber)
            intent.putExtra(LASTCALLED,lastCalled.toString())
            intent.putExtra(FREQUENCY,frequency)
            return intent
        }
    }


}