package com.hllabs.linuxnews

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {


    var currentCheckedArray:BooleanArray = BooleanArray(16 , init = {true})

    var selectedSites:ArrayList<String> = Sites().siteUrls

    var listToShow:ArrayList<NewsArticle> = arrayListOf()

    var feedResultsCtr = 16

    var t1:Long = 0
    var t2:Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FeedItemsBus.INSTANCE.toObserverable().subscribe({
            listToShow.addAll(it)

            feedResultsCtr--
            if(feedResultsCtr <= 0) sortFeed()

        })
        reloadArticles()
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


    fun reloadArticles(){
        for (site in selectedSites){
            FeedLoaderTask(site,"Omg ubuntu").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    fun sortFeed(){
        feedResultsCtr = 0
        //var sortedList = listToShow.sortedWith(compareBy({it.pubDate}))
        var sortedList = listToShow.sortedWith(compareByDescending { it.pubDate })

        listToShow.clear()
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
            for(i in 0 until 16){
                currentCheckedArray[i] = dialogView.listView.isItemChecked(i)

                if(currentCheckedArray[i]) {
                    selectedSites.add(Sites().siteUrls[i])
                    feedResultsCtr++
                }

            }
            reloadArticles()

        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }


}
