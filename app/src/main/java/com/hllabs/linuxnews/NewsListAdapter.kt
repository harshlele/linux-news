package com.hllabs.linuxnews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*

class NewsListAdapter(var newsItems:ArrayList<NewsArticle> , val context: Context): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsItems[position]

        if(item.imgLink != "") {
            holder.headerImgView.visibility = View.VISIBLE
            Picasso.get().load(item.imgLink).into(holder.headerImgView)
        }
        else holder.headerImgView.visibility = View.GONE

        if(item.title != "") holder.titleTextView.text = item.title

        if(item.siteName != "") holder.siteNameText.text = item.siteName

        holder.pubDateText.text = DateUtils.getRelativeTimeSpanString(item.pubDate.time, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS)

        if(item.description != "") holder.descText.text = item.description

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
        var descText = view.articleDescText
    }


}