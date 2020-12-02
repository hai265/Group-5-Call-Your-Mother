package com.example.callmotherapplicationtest

import android.app.ListActivity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import java.io.*
import java.sql.Time
import java.text.ParseException
import java.util.*


class MainActivity : ListActivity() {

    internal lateinit var mAdapter: ContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = ContactAdapter(applicationContext)

        findViewById<FloatingActionButton>(R.id.addContactButton).setOnClickListener { view ->
            //TODO - Implement adding contact to calling circle functionality
        }


        //TODO - Set OnItemClickListener on a contact so an options menu pops up




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

        private val FILE_NAME = "TodoManagerActivityData.txt"
    }

}