package com.example.callmotherapplicationtest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.CallLog
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*

class ContactSettingsActivity : Activity() {

    private var mFrequencyText: TextView? = null
    private var mPhoneNumberText: TextView? = null
    private var mEditTextPhoneNumber: TextView? = null
    private var mEditTextFrequency: EditText? = null
    private var mEditTextPersonName: TextView? = null


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.contact_settings)

        mFrequencyText = findViewById<View>(R.id.frequencyText) as TextView
        mPhoneNumberText = findViewById<View>(R.id.phoneNumberText) as TextView
        mEditTextPhoneNumber = findViewById<View>(R.id.editTextPhoneNumber) as TextView
        mEditTextFrequency = findViewById<View>(R.id.editTextFrequency) as EditText
        mEditTextPersonName = findViewById<View>(R.id.editTextTextPersonName) as TextView
        mEditTextPhoneNumber!!.setText(intent.getStringExtra(ContactDetails.PHONENUMBER).toString())
        mEditTextPersonName!!.setText(intent.getStringExtra(ContactDetails.NAME).toString())
        mEditTextFrequency!!.setText(intent.getIntExtra(ContactDetails.FREQUENCY,1).toString())

        val cancelButton = findViewById<View>(R.id.cancelButton) as Button
        cancelButton.setOnClickListener{
            Log.i(TAG, "pressed cancel")
            finish()
        }

        val saveButton = findViewById<View>(R.id.saveButton) as Button
        saveButton.setOnClickListener{
            Log.i(TAG, "pressed save")

            val name = mEditTextPersonName!!.text.toString()
            val phoneNumber = mEditTextPhoneNumber!!.text.toString()
            var lastCalled = Calendar.getInstance().getTime()

            //Based on a code snippet on StackOverFlow
            // https://stackoverflow.com/questions/6786666/how-do-i-access-call-log-for-android
            val cursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null,
                null, null, null)

            val number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val date = cursor!!.getColumnIndex(CallLog.Calls.DATE)

            //moves through the phone log
            while (cursor.moveToNext()) {

                val phNumber = PhoneNumberUtils.formatNumber(cursor.getString(number),Locale.getDefault().getCountry())
                val callDate = cursor.getString(date)
                val callDayTime = Date(java.lang.Long.valueOf(callDate))
                val updateDate =  ContactDetails.FORMAT.parse(callDayTime.toString())

                if(phoneNumber.equals(phNumber)){
                    lastCalled = updateDate
                }
            }
            cursor.close()

            var returnIntent = ContactDetails.packageToIntent(name,phoneNumber, lastCalled,
                mEditTextFrequency!!.text.toString().toInt())
            setResult(RESULT_OK,returnIntent)
            finish()
        }

        val deleteButton = findViewById<View>(R.id.deleteButton) as Button
        deleteButton.setOnClickListener{
            Log.i(TAG, "pressed delete")
            setResult(MainActivity.DELETE)
            finish()

        }
    }
    companion object{
        val TAG = "contactsettingsactivity"
    }
}