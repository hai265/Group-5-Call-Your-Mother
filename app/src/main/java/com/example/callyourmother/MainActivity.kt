package com.example.callmotherapplicationtest

import android.app.*
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*
import java.util.*


class MainActivity : ListActivity() {

    private lateinit var mAdapter: ContactAdapter
    private lateinit var mAlarmManager: AlarmManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        mAdapter = ContactAdapter(applicationContext)
        listView.adapter = mAdapter

        findViewById<FloatingActionButton>(R.id.addContactButton).setOnClickListener { view ->
            //TODO - Implement adding contact to calling circle functionality
            pickContact()
        }


        //Updates the last call date of the ContactDetails
        val cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
            null, null, null)

        val number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
        val date = cursor!!.getColumnIndex(CallLog.Calls.DATE)
        val numberChecked = ArrayList<String>()

        //goes through the phone log and get the information
        while (cursor.moveToNext()) {

            val phNumber = cursor.getString(number)
            val callDate = cursor.getString(date)
            val updateDate =  ContactDetails.FORMAT.parse(callDate)

            //runs through the adapter to see if the phone number matches any in the adapter
            for (idx in 0 until mAdapter.count) {

                var contact = mAdapter.getItem(idx) as ContactDetails

                //if matches, and have not already been checked, will update the last call date
                if(contact.phoneNumber.equals(phNumber) && !numberChecked.contains(phNumber)){
                    contact.updateLastCalled(updateDate)
                    numberChecked.add(phNumber)
                }

            }
        }
        cursor.close()

        //TODO - Set OnItemClickListener on a contact so an options menu pops up
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val contact = mAdapter.getItem(position) as ContactDetails
                if(contact.name != null && contact.phoneNumber != null && contact.frequency != null && contact.lastCalled != null){
                    val intent = ContactDetails.packageToIntent(contact.name!!,contact.phoneNumber!!,contact.lastCalled,
                        contact.frequency!!
                    )
                    val startIntent = Intent(this@MainActivity, ContactSettingsActivity::class.java)
                    lastContactClicked = position
                    startActivityForResult(startIntent.putExtras(intent), CLICK_CONTACT_REQUEST)
                }
            }

        //Load items if necessary
        if (mAdapter.count == 0)
            loadItems()
        Log.i(TAG, "loaded items")

        mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun pickContact(){
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        intent.resolveActivity(packageManager)?.let {

            startActivityForResult(intent, PICK_CONTACT_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == PICK_CONTACT_REQUEST){
            if (data != null) {
                var cursor: Cursor? = null
                try {
                    var phoneNo: String? = null
                    var name: String? = null
                    // getData() method will have the Content Uri of the selected contact
                    val uri: Uri? = data.data
                    //Query the content uri
                    cursor = uri?.let { getContentResolver().query(it, null, null, null, null) };
                    cursor?.moveToFirst()
                    // column index of the phone number
                    val phoneIndex: Int =
                        cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    // column index of the email

                    // column index of the contact name
                    val nameIndex: Int =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                    phoneNo = cursor.getString(phoneIndex)
                    name = cursor.getString(nameIndex)
                    // Set the value to the textviews

                    Log.i(TAG,"phone number: $phoneNo")
                    Log.i(TAG,"name: $name")

                    //open contact settings activity page
                    val startContactSettingsActivityIntent = Intent(this@MainActivity,ContactSettingsActivity::class.java)
                    startContactSettingsActivityIntent.putExtra(ContactDetails.NAME,name)
                    startContactSettingsActivityIntent.putExtra(ContactDetails.PHONENUMBER,phoneNo)
                    startActivityForResult(startContactSettingsActivityIntent, ADD_CONTACT_REQUEST)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        //Pressed "submit" on the contact settings page
        else if (resultCode == RESULT_OK && requestCode == ADD_CONTACT_REQUEST){
            val returnedIntent = Intent(data)
            val createdToDoItem = ContactDetails(returnedIntent)
            mAdapter.add(createdToDoItem)
            mAdapter.notifyDataSetChanged()
            Log.i(TAG, "Created toDoItem added")
        }

        //Pressed "cancel" on the contact settings page
        if(resultCode == DELETE ) {
            deleteContact(lastContactClicked)
        }
    }
    private fun deleteContact (position : Int){
        Log.i(TAG, "Removed contact $position")
        mAdapter.remove(position)
    }






    // Notification to remind user to call someone. Tapping on the notification will open the phone app to call the person.
    fun notify(name : String, phoneNumber : String){
        val mCallIntent = Intent(Intent.ACTION_DIAL,Uri.parse(phoneNumber))

        val mContentIntent = PendingIntent.getActivity(applicationContext, 0, mCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = Notification.Builder(
            applicationContext, CHANNEL_ID
        )
            .setTicker("tickerText")
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setContentTitle("Call $name")
            .setContentText("Reminder to call $name, tap to call now.")
            .setContentIntent(mContentIntent)
        mNotificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }



    override fun onPause() {
        super.onPause()

        // Save ToDoItems

        saveItems()
        Log.i(TAG, "saved items")
    }

    private fun loadItems() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            Log.d(TAG, getFileStreamPath(FILE_NAME).toString())
            reader = BufferedReader(InputStreamReader(fis))

            var name: String? = null
            var phoneNumber: String? = null
            var frequencey: String? = null
            var lastCalled: Date? = null

            do {
                name = reader.readLine();
                if (name == null)
                    break
                //image = reader.readLine()
                phoneNumber = reader.readLine()
                frequencey = reader.readLine()
                lastCalled = ContactDetails.FORMAT.parse(reader.readLine())
                mAdapter.add(ContactDetails(name, phoneNumber, lastCalled,frequencey))

            }
            while (true)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun saveItems(){
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(
                BufferedWriter(
                    OutputStreamWriter(
                fos)
                )
            )

            for (idx in 0 until mAdapter.count) {

                writer.println(mAdapter.getItem(idx))

            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

    companion object{
        private var hasPermission: Boolean = false
        private val SECONDS = 1000
        const val CLICK_CONTACT_REQUEST = 32123
        const val ADD_CONTACT_REQUEST = 3224
        val PICK_CONTACT_REQUEST = 1
        val DELETE = 189
        val TAG = "Group-5-Call-Your-Mother"
        val CHANNEL_ID = "channel_01"
        val NOTIFICATION_ID = 0
        private lateinit var mNotificationManager: NotificationManager
        private val FILE_NAME = "ContactsData.txt"
        private var lastContactClicked = 0

    }

}