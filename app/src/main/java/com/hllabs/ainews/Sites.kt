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
            "Facebook Research"
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
            "https://research.fb.com/feed/"
            )


    //array of default selections
    val defaultCheckedArray = booleanArrayOf(false,false,true,false,true,true,true,true,true,true)

    val defaultSiteSelection = arrayListOf("http://news.mit.edu/rss/topic/robotics",
            "https://www.technologyreview.com/c/computing/rss/",
            "https://deepmind.com/blog/feed/basic/",
            "http://feeds.feedburner.com/nvidiablog",
            "http://feeds.feedburner.com/blogspot/gJZg",
            "https://blogs.technet.microsoft.com/machinelearning/feed/",
            "https://research.fb.com/feed/"
            )

    //get a site's name from its feed url
    fun getSiteNameFromUrl(url:String):String{
        val index = siteUrls.indexOf(url)
        return siteNames[index].toString()
    }

}