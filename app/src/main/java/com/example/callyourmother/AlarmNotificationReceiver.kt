package com.example.callyourmother

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log

import androidx.core.content.ContextCompat.getSystemService
import com.example.callmotherapplicationtest.ContactDetails
import com.example.callmotherapplicationtest.MainActivity
import com.example.callmotherapplicationtest.R

class AlarmNotificationReceiver: BroadcastReceiver() {

    private lateinit var mNotificationManager: NotificationManager

    // Notification Text Elements
    private val tickerText = "Are You Playing Angry Birds Again!"
    private val contentTitle = "A Kind Reminder"
    private val contentText = "Get back to studying!!"

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


        val mNotificationIntent = Intent(mContext, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val mContentIntent = PendingIntent.getActivity(
            context, 0,
            mCallIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = Notification.Builder(
            mContext, MainActivity.CHANNEL_ID
        )
            .setTicker("tickerText")
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = mContext.getString(R.string.channel_name)
            val descriptionText = mContext.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
            }
            // Register the channel with the system

            mNotificationManager.createNotificationChannel(channel)

        }
    }
}