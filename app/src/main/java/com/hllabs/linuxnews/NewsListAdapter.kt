package com.hllabs.linuxnews

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*

/*
* Adapter for list of articles
* */
class NewsListAdapter(var newsItems:ArrayList<NewsArticle> , val context: Context): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsItems[position]

        //if an image is available, load it. Else,hide the view
        if(item.imgLink != "") {
            holder.headerImgView.visibility = View.VISIBLE
            Picasso.get().load(item.imgLink).into(holder.headerImgView)
        }
        else holder.headerImgView.visibility = View.GONE

        if(item.title != "") holder.titleTextView.text = item.title

        if(item.siteName != "") holder.siteNameText.text = item.siteName

        //Convert Date to relative, and set it
        holder.pubDateText.text = DateUtils.getRelativeTimeSpanString(item.pubDate.time, System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS)

        if(item.description != "") holder.descText.text = item.description

        //set click listener for when item is clicked
        holder.newsCard.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val i = Intent(context,WebPageActivity::class.java)
                i.putExtra("i" , newsItems[position])
                context.startActivity(i)
            }

        })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }


    fun addItems(items: ArrayList<NewsArticle>){
        newsItems.addAll(items)
        notifyDataSetChanged()
    }

    fun clearList(){
        newsItems.clear()
        notifyDataSetChanged()
    }

    class NewsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val headerImgView = view.articleHeaderImage
        val titleTextView = view.articleTitleTextView
        val siteNameText = view.articleSiteName
        val pubDateText = view.articleDate
        val descText = view.articleDescText
        val newsCard = view.newsCard
    }


}