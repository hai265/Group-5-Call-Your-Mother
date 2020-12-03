package com.example.callmotherapplicationtest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*

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



        mEditTextPhoneNumber!!.setText(intent.getStringExtra(ContactDetails.PHONENUMBER).toString())
        mEditTextPersonName!!.setText(intent.getStringExtra(ContactDetails.NAME).toString())
        mEditTextTime!!.setText(Date().toString())
        mEditTextTime!!



        val cancelButton = findViewById<View>(R.id.cancelButton) as Button
        cancelButton.setOnClickListener{
            Log.i(TAG, "pressed cancel")
            finish()
        }

        val saveButton = findViewById<View>(R.id.saveButton) as Button
        saveButton.setOnClickListener{
            Log.i(TAG, "pressed submit")

            val name = mEditTextPersonName!!.text.toString()
            val phoneNumber = mEditTextPhoneNumber!!.text.toString()
            var lastCalled = Calendar.getInstance().getTime()

            val cursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null,
                null, null, null)

            val number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val date = cursor!!.getColumnIndex(CallLog.Calls.DATE)

            while (cursor.moveToNext()) {

                val phNumber = cursor.getString(number)
                val callDate = cursor.getString(date)
                val updateDate =  ContactDetails.FORMAT.parse(callDate)

                if(phoneNumber.equals(phNumber)){
                    lastCalled = updateDate
                    break
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
            finish()

        }
    }
    companion object{
        val TAG = "contactsettingsactivity"
    }
}