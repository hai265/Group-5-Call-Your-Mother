package com.example.callmotherapplicationtest

import android.content.Intent
import android.graphics.Bitmap

import java.sql.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ContactDetails {
    var name : String? = null
   // var image: Bitmap? = null
    var phoneNumber : String? = null
    var frequency :Int? = null
    var lastCalled = Date()
    var isLate : Boolean = false

    internal constructor(name: String, phoneNumber: String, lastCalled: Date, frequency : String){

        this.name = name
        this.phoneNumber = phoneNumber
        this.lastCalled = lastCalled
        this.frequency = frequency.toInt()

        var currentTime = Calendar.getInstance().getTime()
        isLate = currentTime.after(lastCalled)


    }

    internal constructor(intent: Intent){
        name = intent.getStringExtra(NAME)
       // image = intent.getParcelableExtra(IMAGE)
        phoneNumber = intent.getStringExtra(PHONENUMBER)
        //pass in the time as a toString first
        try {
            lastCalled = FORMAT.parse(intent.getStringExtra(LASTCALLED))
        } catch (e: ParseException) {
            lastCalled = Date()
        }
    }

    override fun toString(): String {
        return (name + ITEM_SEP + phoneNumber + ITEM_SEP + frequency + ITEM_SEP
                + FORMAT.format(lastCalled))
    }

    fun updateLastCalled(update:Date){

        lastCalled = update
    }


    companion object{

        val ITEM_SEP = System.getProperty("line.separator")
        val NAME = "name"
      //  val IMAGE = "image"
        val PHONENUMBER = "phonenumber"
        val LASTCALLED = "lastCalled"
        val FREQUENCY = "frequency"

        val FORMAT = SimpleDateFormat(
            "EEE MMM d HH:mm:ss zzz yyyy", Locale.US)

        fun packageToIntent(name : String, phoneNumber: String, lastCalled: Date, frequency: Int) : Intent{

            val intent = Intent()
            intent.putExtra(NAME,name)
            //intent.putExtra(IMAGE,image)
            intent.putExtra(PHONENUMBER,phoneNumber)
            intent.putExtra(LASTCALLED,lastCalled.toString())
            intent.putExtra(FREQUENCY,frequency)
            return intent
        }
    }


}