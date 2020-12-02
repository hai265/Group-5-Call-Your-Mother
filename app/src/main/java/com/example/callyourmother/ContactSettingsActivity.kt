package com.example.callmotherapplicationtest

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ContactSettingsActivity : Activity() {

    //private var mDeleteButton: Button? = null
    //private var mCancelButton: Button? = null
    // private var mSaveButton: Button? = null
    private var mFrequencyText: TextView? = null
    private var mPhoneNumberText: TextView? = null
    private var mTimeText: TextView? = null
    private var mEditTextPhoneNumber: EditText? = null
    private var mEditTextFrequency: EditText? = null
    private var mEditTextTime: EditText? = null
    private var mEditTextPersonName: EditText? = null





    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO Implement the contact settings activity

        setContentView(R.layout.contact_settings)



        //mDeleteButton = findViewById<View>(R.id.deleteButton) as Button
        //mCancelButton = findViewById<View>(R.id.cancelButton) as Button
        //mSaveButton = findViewById<View>(R.id.saveButton) as Button
        mFrequencyText = findViewById<View>(R.id.frequencyText) as TextView
        mPhoneNumberText = findViewById<View>(R.id.phoneNumberText) as TextView
        mTimeText = findViewById<View>(R.id.timeText) as TextView
        mEditTextPhoneNumber = findViewById<View>(R.id.editTextPhoneNumber) as EditText
        mEditTextFrequency = findViewById<View>(R.id.editTextFrequency) as EditText
        mEditTextTime = findViewById<View>(R.id.editTextTime) as EditText
        mEditTextPersonName = findViewById<View>(R.id.editTextTextPersonName) as EditText





        val cancelButton = findViewById<View>(R.id.cancelButton) as Button
        cancelButton.setOnClickListener{
            finish()
        }






    }
}