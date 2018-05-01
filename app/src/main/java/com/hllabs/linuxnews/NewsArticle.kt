package com.hllabs.linuxnews

import java.util.*

data class NewsArticle(
            var feedUrl:String = "", var siteName:String = "", var title:String = "", var author:String = "",
            var description:String = "", var content:String = "", var imgLink:String = "", var link:String = "",
            var pubDate: Date = Date(), var categories:List<String> = arrayListOf()
        )
