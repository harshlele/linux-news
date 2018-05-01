package com.hllabs.linuxnews


/*
*List of site names and URLs.
 */
class Sites{
    val siteNames:Array<CharSequence> = arrayOf("Omgubuntu" , "bits.debian.org" , "Slashdot Linux" , "Linux.com" ,
            "Linux Insider" , "Planet Gentoo" , "Arch Linux News" , "Lubuntu Blog" ,
            "Ubuntu Mate Blog" , "Kubuntu Blog" , "Ubuntu Insights" , "r/Linux" ,
            "r/opensource" , "r/ubuntu" , "r/archlinux" , "r/debian"
    )
    val siteUrls:ArrayList<String> = arrayListOf("http://feeds.feedburner.com/d0od" ,
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
                                        "https://www.reddit.com/r/linux/.rss",
                                        "https://www.reddit.com/r/opensource/.rss",
                                        "https://www.reddit.com/r/ubuntu/.rss",
                                        "https://www.reddit.com/r/archlinux/.rss",
                                        "https://www.reddit.com/r/debian/.rss"
    )


    fun getSiteNameFromUrl(url:String):String{
        val index = siteUrls.indexOf(url)
        return siteNames[index].toString()
    }

}