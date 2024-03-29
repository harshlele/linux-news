package com.hllabs.linuxnews

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL



/*
* Main Activity
* */
class MainActivity : AppCompatActivity() {

    //Array of booleans that holds whether a news site is selected or not
    //By default all sites are selected
    var currentCheckedArray = Sites().defaultCheckedArray

    //array of selected site urls
    //By default, all sites are selected
    var selectedSiteUrls:ArrayList<String> = Sites().defaultSiteSelection

    //The list of news articles from all selected sites
    var combinedArticleList:ArrayList<NewsArticle> = arrayListOf()

    //counter for feed sources that is decremented every time a feed has loaded
    var feedSourcesCtr:Float = Sites().defaultSiteSelection.size.toFloat()

    //total no of feeds
    var totalFeedSources:Float = Sites().defaultSiteSelection.size.toFloat()

    //for measuring time
    var t1:Long = 0
    var t2:Long = 0

    var isSearching = false
    var searchedText = ""

    lateinit var adRequest:AdRequest

    var TAG = "LOG!"

    //GDPR consent form
    lateinit var form: ConsentForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Listener for Event bus
        FeedItemsBus.INSTANCE.toObserverable().subscribe({
            //Add it to the combined list
            combinedArticleList.addAll(it)

            //if articles from all sites have been retrieved, sort them
            feedSourcesCtr--

            //calculate percent of feeds loaded and display them on the progress view
            val percent:Int = (((totalFeedSources - feedSourcesCtr)/totalFeedSources )*100).toInt()
            waveLoadingView.progressValue = percent
            waveLoadingView.centerTitle = "$percent %"
            if(feedSourcesCtr <= 0) sortFeed()

        })

        //initialise recyclerview
        newsList.layoutManager = LinearLayoutManager(this)
        newsList.adapter = NewsListAdapter(combinedArticleList,this)


        reloadArticles()

        //initialise mobile ads
        MobileAds.initialize(this,"ca-app-pub-7444749934962149~4885084340")
        val consentInfo = ConsentInformation.getInstance(applicationContext)
        val pubIds = arrayOf("pub-7444749934962149")
        //update consent info
        consentInfo.requestConsentInfoUpdate(pubIds,object : ConsentInfoUpdateListener{


            //if the user is in the EU, show the consent dialog
            // (regardless of whether the consent info is sucessfully updated or not)
            override fun onFailedToUpdateConsentInfo(reason: String?) {
                Log.d(TAG,"onFailedToUpdateConsentInfo: $reason")
                if(consentInfo.isRequestLocationInEeaOrUnknown) showConsentDialog()
            }

            override fun onConsentInfoUpdated(consentStatus: ConsentStatus?) {

                if(consentInfo.isRequestLocationInEeaOrUnknown){

                    when (consentStatus) {
                        ConsentStatus.PERSONALIZED -> {
                            adRequest = AdRequest.Builder().build()
                            adView.loadAd(adRequest)

                        }
                        ConsentStatus.NON_PERSONALIZED -> {
                            val extras = Bundle()
                            extras.putString("npa", "1")
                            adRequest = AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java , extras).build()
                            adView.loadAd(adRequest)
                        }
                        else -> showConsentDialog()
                    }
                }
                else{
                    adRequest = AdRequest.Builder().build()
                    adView.loadAd(adRequest)

                }
            }

        })
    }

    //Show consent dialog
    fun showConsentDialog(){

        val privPolicyUrl = URL("https://hllabs.github.io/linuxnews/privacy_policy")


        form = ConsentForm.Builder(this@MainActivity, privPolicyUrl)
                .withListener(object : ConsentFormListener() {
                    //show the form only when it has been loaded
                    override fun onConsentFormLoaded() {
                       form.show()
                    }
                    override fun onConsentFormOpened() {}


                    override fun onConsentFormClosed(
                            consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                        when (consentStatus) {
                            //if the consent status is personalised, show personalised ads.
                            // For all other values, show non-personalised ads
                            ConsentStatus.PERSONALIZED -> {
                                adRequest = AdRequest.Builder().build()
                                adView.loadAd(adRequest)

                            }
                            else -> {
                                val extras = Bundle()
                                extras.putString("npa", "1")
                                adRequest = AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java , extras).build()
                                adView.loadAd(adRequest)
                            }

                        }
                    }

                    override fun onConsentFormError(errorDescription: String?) {
                        Log.d(TAG,"onConsentFormError: $errorDescription")
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build()

        form.load()
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
            else if(item.itemId == R.id.action_search){

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Search For: ")
                val editText = EditText(this@MainActivity)
                editText.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                builder.setView(editText)

                builder.setPositiveButton("Search",{dialogInterface, i ->
                    searchedText = editText.text.toString()
                    isSearching = true

                    feedSourcesCtr = 0f
                    for(feed in selectedSiteUrls){feedSourcesCtr+=1}

                    totalFeedSources = feedSourcesCtr


                    reloadArticles()
                })

                val dialog:AlertDialog = builder.create()

                dialog.show()
            }
            else if(item.itemId == R.id.action_refresh){
                feedSourcesCtr = 0f
                for(feed in selectedSiteUrls){feedSourcesCtr+=1}

                totalFeedSources = feedSourcesCtr


                reloadArticles()
            }
            else if(item.itemId == R.id.action_downloads){
                val intent = Intent( this,OfflineActivity::class.java)
                startActivity(intent)
            }
            else if(item.itemId == R.id.action_ad_settings) showConsentDialog()

            else if(item.itemId == R.id.action_privacy_policy){
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://hllabs.github.io/linuxnews/privacy_policy")))
            }


        }

        return super.onOptionsItemSelected(item)
    }

    //Start a new AsyncTask for every selected site and run them in parallel
    fun reloadArticles(){
        (newsList.adapter as NewsListAdapter).clearList()

        waveLoadingView.visibility = View.VISIBLE
        waveLoadingView.progressValue = 0

        //if no sites have been selected,show a no site selected loading animation
        if(selectedSiteUrls.size == 0){

            waveLoadingView.centerTitle = "No site selected"
            waveLoadingView.startAnimation()

        }

        else {

            //Show the loading animation
            waveLoadingView.visibility = View.VISIBLE
            waveLoadingView.progressValue = 0
            waveLoadingView.centerTitle = "0 %"
            waveLoadingView.startAnimation()
            //start asynctask for each site
            for (url in selectedSiteUrls) {
                FeedLoaderTask(url, Sites().getSiteNameFromUrl(url)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            }
        }
    }


    //Sort the article list by publication date in reverse order
    fun sortFeed(){
        feedSourcesCtr = 0f
        val sortedList = ArrayList(combinedArticleList.sortedWith(compareByDescending { it.pubDate }))

        combinedArticleList.clear()

        if(isSearching){


            val iterator = sortedList.iterator()

            while (iterator.hasNext()){
                val item = iterator.next()
                if( !item.title.contains(searchedText,true) &&
                !item.description.contains(searchedText,true) &&
                !item.siteName.equals(searchedText,true) &&
                !item.author.equals(searchedText,true)
                ){
                    iterator.remove()
                }

            }
            isSearching = false
            searchedText = ""
        }

        (newsList.adapter as NewsListAdapter).addItems(sortedList)

        waveLoadingView.cancelAnimation()
        waveLoadingView.visibility = View.GONE

    }

    //Show a dialog that allows the user to choose which sites to see news from
    fun showFilterDialog(){

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Filter News Sites")

        builder.setPositiveButton("OK" , null)


        builder.setNeutralButton("Select All" , {dialogInterface, i ->
            val dialogView = dialogInterface as AlertDialog
            for(i in 0 until Sites().siteUrls.size) dialogView.listView.setItemChecked(i,true)
        })

        builder.setNegativeButton("Select None" , {dialogInterface, i ->
            val dialogView = dialogInterface as AlertDialog
            for(i in 0 until Sites().siteUrls.size) dialogView.listView.setItemChecked(i,false)
        })

        builder.setMultiChoiceItems(Sites().siteNames, currentCheckedArray , { dialogInterface, i, b -> })

        //listener for when the dialog is dismissed, or closed
        builder.setOnDismissListener { dialogInterface ->

            val dialogView = dialogInterface as AlertDialog
            //clear the selected sites list to repopulate them again
            selectedSiteUrls.clear()
            for(i in 0 until Sites().siteUrls.size){
                currentCheckedArray[i] = dialogView.listView.isItemChecked(i)

                if(currentCheckedArray[i]) {
                    selectedSiteUrls.add(Sites().siteUrls[i])
                    //increment the counter
                    feedSourcesCtr++
                }

            }

            totalFeedSources = feedSourcesCtr

            //reload with the new list of sites
            reloadArticles()

        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }


}
