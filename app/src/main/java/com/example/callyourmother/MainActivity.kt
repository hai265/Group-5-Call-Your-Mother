package com.example.callmotherapplicationtest

import android.app.ListActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView



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

    private fun loadItems(){
        //TODO - Implement similar to lab 4's load items to retrieve the list information
    }
    private fun saveItems(){
        //TODO - Implement similar to lab 4's load items to store the list information
    }

}