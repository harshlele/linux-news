package com.hllabs.linuxnews


/*
*List of site names and URLs.
 */
class Sites{
    val siteNames:Array<CharSequence> = arrayOf("Distrowatch","Omgubuntu" , "bits.debian.org" , "Slashdot Linux" , "Linux.com" ,
            "Linux Insider" , "Planet Gentoo" , "Arch Linux News" , "Lubuntu Blog" ,
            "Ubuntu Mate Blog" , "Kubuntu Blog" , "Ubuntu Insights" , "Linux Today" , "Linux Journal"
    )
    val siteUrls:ArrayList<String> = arrayListOf("https://distrowatch.com/news/dw.xml",
                                        "http://feeds.feedburner.com/d0od" ,
                                        "https://bits.debian.org/feeds/feed.rss" ,
                                        "http://rss.slashdot.org/Slashdot/slashdotMain",
                                        "https://www.linux.com/feeds/rss",
                                        "https://www.linuxinsider.com/perl/syndication/rssfull.pl",
                                        "https://planet.gentoo.org/rss20.xml",
                                        "https://www.archlinux.org/feeds/news/",
                                        "https://lubuntu.me/feed/",
                                        "https://ubuntu-mate.org/rss.xml",
                                        "https://kubuntu.org/feed/",
                                        "https://insights.ubuntu.com/feed",
                                        "http://feeds.feedburner.com/linuxtoday/linux?format=xml",
                                        "https://www.linuxjournal.com/news/feed"
    )


    //array of default selections
    val defaultCheckedArray = booleanArrayOf(true , true, false, true, false, true , false, false, false, true, false, true , false, false)

    val defaultSiteSelection = arrayListOf<String>("https://distrowatch.com/news/dw.xml","http://feeds.feedburner.com/d0od" , "http://rss.slashdot.org/Slashdot/slashdotMain",
                                                    "https://www.linuxinsider.com/perl/syndication/rssfull.pl","https://ubuntu-mate.org/rss.xml",
                                                    "https://insights.ubuntu.com/feed"
                                                    )
    //get a site's name from its feed url
    fun getSiteNameFromUrl(url:String):String{
        val index = siteUrls.indexOf(url)
        return siteNames[index].toString()
    }

}