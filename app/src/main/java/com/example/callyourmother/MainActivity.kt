package com.example.callmotherapplicationtest

import android.app.*
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*
import java.util.*


class MainActivity : ListActivity() {

    internal lateinit var mAdapter: ContactAdapter
    private lateinit var mNotificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        listView.adapter = ContactAdapter(this)
        setContentView(R.layout.activity_main)
        mAdapter = ContactAdapter(applicationContext)

        findViewById<FloatingActionButton>(R.id.addContactButton).setOnClickListener { view ->
            //TODO - Implement adding contact to calling circle functionality
            pickContact()
        }

        //TODO - Set OnItemClickListener on a contact so an options menu pops up

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

                    val startContactSettingsActivityIntent = Intent(applicationContext,ContactSettingsActivity::class.java)
                    startContactSettingsActivityIntent.putExtra(ContactDetails.NAME,name)
                    startContactSettingsActivityIntent.putExtra(ContactDetails.PHONENUMBER,phoneNo)
                    startActivity(startContactSettingsActivityIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }
    }

    //choose a contact
    private fun contactPicked(data: Intent) {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val mNotificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(channel)
        }
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

    public override fun onResume() {
        super.onResume()

        // Load saved ToDoItems, if necessary

        if (mAdapter.count == 0)
            loadItems()
    }

    override fun onPause() {
        super.onPause()

        // Save ToDoItems

        saveItems()

    }


    private fun loadItems() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))

            var name: String? = null
            var phoneNumber: String? = null
            var frequencey: String? = null
            var timeToRemind: Date? = null

            do {
                name = reader.readLine();
                if (name == null)
                    break
                //image = reader.readLine()
                phoneNumber = reader.readLine()
                frequencey = reader.readLine()
                timeToRemind = ContactDetails.FORMAT.parse(reader.readLine())
                mAdapter.add(ContactDetails(name, phoneNumber, timeToRemind,frequencey))

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

    companion object {
        private var hasPermission: Boolean = false
        val PICK_CONTACT_REQUEST = 1
        val TAG = "Group-5-Call-Your-Mother"
        val CHANNEL_ID = "channel_01"
        val NOTIFICATION_ID = 0
        private val FILE_NAME = "ContactsData.txt"
    }

}