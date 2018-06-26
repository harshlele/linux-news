package com.hllabs.ainews


/*
*List of site names and URLs.
 */
class Sites{
    val siteNames:Array<CharSequence> = arrayOf(
            "Science Daily - AI",
            "Science Daily - Neural Interfaces",
            "MIT Blog",
            "Phys.org",
            "MIT Technology Review",
            "Deepmind Blog",
            "Nvidia Blog",
            "Google AI Blog",
            "Microsoft Technet - Machine Learning Blog",
            "Youtube - Siraj Raval",
            "Youtube - Tensorflow"

    )
    val siteUrls:ArrayList<String> = arrayListOf(
            "https://www.sciencedaily.com/rss/computers_math/artificial_intelligence.xml",
            "https://www.sciencedaily.com/rss/computers_math/neural_interfaces.xml",
            "http://news.mit.edu/rss/topic/robotics",
            "https://phys.org/rss-feed/technology-news/computer-sciences/",
            "https://www.technologyreview.com/c/computing/rss/",
            "https://deepmind.com/blog/feed/basic/",
            "http://feeds.feedburner.com/nvidiablog",
            "http://feeds.feedburner.com/blogspot/gJZg",
            "https://blogs.technet.microsoft.com/machinelearning/feed/",
            "https://www.youtube.com/feeds/videos.xml?channel_id=UCWN3xxRkmTPmbKwht9FuE5A",
            "https://www.youtube.com/feeds/videos.xml?channel_id=UC0rqucBdTuFTjJiefW5t-IQ"
    )


    //array of default selections
    val defaultCheckedArray = booleanArrayOf(false,false,true,false,true,true,true,true,true,true,true)

    val defaultSiteSelection = arrayListOf<String>("http://news.mit.edu/rss/topic/robotics",
            "https://www.technologyreview.com/c/computing/rss/",
            "https://deepmind.com/blog/feed/basic/",
            "http://feeds.feedburner.com/nvidiablog",
            "http://feeds.feedburner.com/blogspot/gJZg",
            "https://blogs.technet.microsoft.com/machinelearning/feed/",
            "https://www.youtube.com/feeds/videos.xml?channel_id=UCWN3xxRkmTPmbKwht9FuE5A",
            "https://www.youtube.com/feeds/videos.xml?channel_id=UC0rqucBdTuFTjJiefW5t-IQ"
            )

    //get a site's name from its feed url
    fun getSiteNameFromUrl(url:String):String{
        val index = siteUrls.indexOf(url)
        return siteNames[index].toString()
    }

}