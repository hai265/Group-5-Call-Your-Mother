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
    var timeToRemind = Date()
    var isLate : Boolean = false

    internal constructor(name: String, phoneNumber: String, timeToRemind: Date, frequency : String){

        this.name = name
        this.phoneNumber = phoneNumber
        this.timeToRemind = timeToRemind
        this.frequency = frequency.toInt()

        var currentTime = Calendar.getInstance().getTime()
        isLate = currentTime.after(timeToRemind)


    }

    internal constructor(intent: Intent){
        name = intent.getStringExtra(NAME)
        // image = intent.getParcelableExtra(IMAGE)
        phoneNumber = intent.getStringExtra(PHONENUMBER)
        //pass in the time as a toString first
        try {
            timeToRemind = FORMAT.parse(intent.getStringExtra(TIMETOREMIND))
        } catch (e: ParseException) {
            timeToRemind = Date()
        }
        frequency = intent.getIntExtra(FREQUENCY,1)
    }

    override fun toString(): String {
        return (name + ITEM_SEP + phoneNumber + ITEM_SEP + frequency + ITEM_SEP
                + FORMAT.format(timeToRemind))
    }


    companion object{

        val ITEM_SEP = System.getProperty("line.separator")
        val NAME = "name"
        //  val IMAGE = "image"
        val PHONENUMBER = "phonenumber"
        val TIMETOREMIND = "timetoremind"
        val FREQUENCY = "frequency"


        val FORMAT = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US)

        fun packageToIntent(name : String, phoneNumber: String, timeToRemind: Date, frequency: Int) : Intent{

            val intent = Intent()
            intent.putExtra(NAME,name)
            //intent.putExtra(IMAGE,image)
            intent.putExtra(PHONENUMBER,phoneNumber)
            intent.putExtra(TIMETOREMIND,timeToRemind.toString())
            intent.putExtra(FREQUENCY,frequency)
            return intent
        }
    }


}