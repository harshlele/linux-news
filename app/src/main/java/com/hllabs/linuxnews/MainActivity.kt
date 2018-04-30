package com.hllabs.linuxnews

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {


    var currentCheckedArray:BooleanArray = BooleanArray(16 , init = {true})

    var selectedSites:ArrayList<String> = Sites().siteUrls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if(item.itemId == R.id.action_filter) {
                showFilterDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun showFilterDialog(){

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Filter News Sources")

        builder.setPositiveButton("OK" , null)

        builder.setNeutralButton("Select All" , {dialogInterface, i ->
            val dialogView = dialogInterface as AlertDialog
            for(i in 0 until 16) dialogView.listView.setItemChecked(i,true)
        })

        builder.setNegativeButton("Select None" , {dialogInterface, i ->
            val dialogView = dialogInterface as AlertDialog
            for(i in 0 until 16) dialogView.listView.setItemChecked(i,false)
        })

        builder.setMultiChoiceItems(Sites().siteNames, currentCheckedArray , { dialogInterface, i, b -> })

        builder.setOnDismissListener { dialogInterface ->

            val dialogView = dialogInterface as AlertDialog
            selectedSites.clear()
            for(i in 0 until currentCheckedArray.size){
                currentCheckedArray[i] = dialogView.listView.isItemChecked(i)

                if(currentCheckedArray[i]){
                    selectedSites.add(Sites().siteUrls[i])
                }

            }

            Log.d("LOG!" , selectedSites.size.toString())
            Log.d("LOG!" , selectedSites.toString())

        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }


}
