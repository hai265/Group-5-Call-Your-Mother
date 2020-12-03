package com.example.callmotherapplicationtest

import android.content.Intent
import android.graphics.Bitmap
import java.sql.Time

class ContactDetails {
    var name : String? = null
    var image: Bitmap? = null
    var phoneNumber : String? = null
    var timeToRemind : Time? = null
    var isLate : Boolean = false

    constructor(intent: Intent){
        name = intent.getStringExtra(NAME)
        image = intent.getParcelableExtra(IMAGE)
        phoneNumber = intent.getStringExtra(PHONENUMBER)
        //pass in the time as a toString first
        timeToRemind = Time.valueOf(intent.getStringExtra(TIMETOREMIND))
    }

    fun packageToIntent() : Intent{

        val intent = Intent()
        intent.putExtra(NAME,name)
        intent.putExtra(IMAGE,image)
        intent.putExtra(PHONENUMBER,phoneNumber)
        intent.putExtra(TIMETOREMIND,timeToRemind.toString())
        return intent
    }

    companion object{

        val NAME = "name"
        val IMAGE = "image"
        val PHONENUMBER = "phonenumber"
        val TIMETOREMIND = "timetoremind"
    }


}