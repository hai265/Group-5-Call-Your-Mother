package com.example.callyourmother

import android.app.NotificationChannel
import android.app.NotificationManager



import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.net.Uri
import android.util.Log



import com.example.callmotherapplicationtest.ContactDetails
import com.example.callmotherapplicationtest.MainActivity
import com.example.callmotherapplicationtest.R

class AlarmNotificationReceiver: BroadcastReceiver() {

    private lateinit var mNotificationManager: NotificationManager

    // Notification Text Elements


    private lateinit var mContext: Context
    private lateinit var mChannelID: String

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null) {
            mContext = context
        }
        val name = intent?.getStringExtra(ContactDetails.NAME)
        Log.i(MainActivity.TAG, "Notification broadcast recieved from $name" )
        val phoneNumber = intent?.getStringExtra(ContactDetails.PHONENUMBER)

        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        //Clicking on notification opens the app (for now)
        val mCallIntent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:$phoneNumber"))




        val mContentIntent = PendingIntent.getActivity(
            context, 0,
            mCallIntent, PendingIntent.FLAG_ONE_SHOT
        )
        //Creating the notification
        val notificationBuilder = Notification.Builder(
            mContext, MainActivity.CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setContentTitle("Call $name")
            .setContentText("Reminder to call $name, tap to call now.")
            .setContentIntent(mContentIntent)
        mNotificationManager.notify(MainActivity.NOTIFICATION_ID,notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

            val name = mContext.getString(R.string.channel_name)
            val descriptionText = mContext.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_MAX

            val channel = NotificationChannel(MainActivity.CHANNEL_ID, name, NotificationManager.IMPORTANCE_MAX).apply {
                description = descriptionText
                enableLights(true)
            }
            // Register the channel with the system

            mNotificationManager.createNotificationChannel(channel)


    }
}