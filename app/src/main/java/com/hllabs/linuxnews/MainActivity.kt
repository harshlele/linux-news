package com.hllabs.linuxnews

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

/*
* Main Activity
* */
class MainActivity : AppCompatActivity() {

    //Array of booleans that holds whether a news site is selected or not
    //By default all sites are selected
    var currentCheckedArray:BooleanArray = BooleanArray(16 , init = {true})

    //array of selected site urls
    //By default, all sites are selected
    var selectedSiteUrls:ArrayList<String> = Sites().siteUrls

    //The list of news articles from all selected sites
    var combinedArticleList:ArrayList<NewsArticle> = arrayListOf()

    var feedResultsCtr = 16

    var t1:Long = 0
    var t2:Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Listener for Event bus
        FeedItemsBus.INSTANCE.toObserverable().subscribe({
            //Add it to the combined list
            combinedArticleList.addAll(it)

            //if articles from all sites have been retrieved, sort them
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

    //Start a new AsyncTask for every selected site and run them in parallel
    fun reloadArticles(){
        for (url in selectedSiteUrls){
            FeedLoaderTask(url,Sites().getSiteNameFromUrl(url)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }


    //Sort the article list by publication date in reverse order
    fun sortFeed(){
        feedResultsCtr = 0
        //var sortedList = combinedArticleList.sortedWith(compareBy({it.pubDate}))
        var sortedList = combinedArticleList.sortedWith(compareByDescending { it.pubDate })

        combinedArticleList.clear()
    }

    //Show a dialog that allows the user to choose which sites to see news from
    fun showFilterDialog(){

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Filter News Sites")

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

        //listener for when the dialog is dismissed, or closed
        builder.setOnDismissListener { dialogInterface ->

            val dialogView = dialogInterface as AlertDialog
            //clear the selected sites list to repopulate them again
            selectedSiteUrls.clear()
            for(i in 0 until 16){
                currentCheckedArray[i] = dialogView.listView.isItemChecked(i)

                if(currentCheckedArray[i]) {
                    selectedSiteUrls.add(Sites().siteUrls[i])
                    //increment the counter
                    feedResultsCtr++
                }

            }
            //reload with the new list of sites
            reloadArticles()

        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }


}
