package com.hllabs.linuxnews

import android.os.AsyncTask
import android.util.Log
import com.prof.rssparser.Article
import com.prof.rssparser.Parser

/*
* AsyncTask to load data from a site's RSS feed
* */
class FeedLoaderTask(val url:String, val name:String ): AsyncTask<Unit, Unit, Unit>() {

    var articleList = ArrayList<NewsArticle>()

    override fun doInBackground(vararg p0: Unit?) {

        val parser = Parser()
        parser.execute(url)
        parser.onFinish(object: Parser.OnTaskCompleted{
            override fun onError() {
                Log.d("LOG!" , "Error")
            }

            override fun onTaskCompleted(list: ArrayList<Article>?) {
                for (article in list!!.iterator()){
                    val a = NewsArticle()
                    a.feedUrl = url
                    a.siteName = name
                    //Not all sites have all data, so check for null before getting properties
                    if(article.title != null)           a.title = article.title
                    if(article.description != null )    a.description = article.description
                    if(article.content != null )        a.content = article.content
                    if(article.image != null )          a.imgLink = article.image
                    if(article.link != null )           a.link = article.link
                    if(article.pubDate != null )        a.pubDate = article.pubDate
                    if(article.categories != null)      a.categories = article.categories

                    articleList.add(a)
                    //Get at most 20 items
                    if(articleList.size >= 20) break
                }
                //Send them over the Event bus to MainActivity
                FeedItemsBus.INSTANCE.send(articleList)
            }

        })


    }

}