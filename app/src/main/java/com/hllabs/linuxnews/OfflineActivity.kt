package com.hllabs.linuxnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_offline.*

class OfflineActivity : AppCompatActivity() {

    var savedArticleList:ArrayList<OfflineNewsObj> = ArrayList()

    var newsObjList:ArrayList<NewsArticle> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline)

        supportActionBar?.setTitle("Saved Pages")

        Paper.init(applicationContext)
        savedArticleList = Paper.book().read("items" , ArrayList())

        for(item in savedArticleList){
            newsObjList.add(item.article)
        }

        offlineArticlesList.layoutManager = LinearLayoutManager(this@OfflineActivity)
        offlineArticlesList.adapter = NewsListAdapter(newsObjList,this@OfflineActivity,true)


    }
}
