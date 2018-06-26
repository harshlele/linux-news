package com.hllabs.ainews

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_web_page.*




/*
* Activity to show the article content
* */
class WebPageActivity : AppCompatActivity() {

    var isFullPageLoaded = true
    var isPageSaved = false

    var isPageOffline = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        val article:NewsArticle

        //get the intent
        if(intent != null) {

            isPageOffline = intent.getBooleanExtra("isOffline",false)

            article = intent.getParcelableExtra<NewsArticle>("i")
            webview.settings.javaScriptEnabled = true
            webview.webChromeClient = WebChromeClient()


            if(!isPageOffline) {

                webview.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        if (isFullPageLoaded && !isPageSaved) btnDownloadPage.visibility = View.VISIBLE
                    }
                }

                btnDownloadPage.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        btnDownloadPage.visibility = View.GONE
                        isPageSaved = true
                        /* This call inject JavaScript into the page */
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML(document.documentElement.outerHTML);")
                    }
                })


                if (article != null) {
                    webview.addJavascriptInterface(JSInterface(applicationContext, article), "HTMLOUT")

                    //if the article content is not available,just load the full site
                    if (article.content == "") {
                        webview.loadUrl(article.link)
                    }
                    //if it is, load just the description and title
                    else {
                        isFullPageLoaded = false
                        btnLoadFullPage.visibility = View.VISIBLE
                        loadContent(article)
                    }

                }

                btnLoadFullPage.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        isFullPageLoaded = true
                        webview.loadUrl(article.link)
                        btnLoadFullPage.visibility = View.GONE
                    }

                })
            }
            else{
                webview.webViewClient = WebViewClient()
                btnLoadFullPage.visibility = View.GONE
                btnDownloadPage.visibility = View.GONE

                Paper.init(applicationContext)
                val offlineList = Paper.book().read("items",ArrayList<OfflineNewsObj>())

                for(i in offlineList){
                    if(i.article.title == article.title){
                        Log.d("LOG2",i.html)
                        webview.loadData(i.html,"text/html","UTF-8")
                    }
                }

            }
        }
    }

    //add a few css rules and title to the description
    fun loadContent(article: NewsArticle){

        val t = article.title
        val a = article.siteName
        var htmlContent = """
                        |               <html>
                        |                   <head>
                        |                       <style>
                        |                           img{
                        |                               display: block;
                        |                               margin-left: auto;
                        |                               margin-right: auto;
                        |                               width: 100%
                        |
                        |                           }
                        |                           div,p,body,code{
                        |                               margin-top: 15px;
                        |                               margin-bottom: 15px;
                        |                               padding-top:15px;
                        |                               padding-bottom:15px
                        |                           }
                        |                           h3{
                        |                               margin-top: 15px;
                        |                               margin-bottom: 15px;
                        |                           }
                        |                           h4{
                        |                               color:grey
                        |                           }
                        |
                        |                       </style>
                        |                   </head>
                        |                   <body>
                        |                      <h3>$t</h3>
                        |                      <h4>$a</h4>
                        |
                        |                   """.trimMargin()

        htmlContent+=article.content
        htmlContent+="</body></html>"

        webview.loadData(htmlContent, "text/html" , "UTF-8")

    }


    class JSInterface(val c:Context,val article: NewsArticle){
        @JavascriptInterface
        fun processHTML(html: String) {
            Paper.init(c)
            val savedItemList = Paper.book().read("items" , ArrayList<OfflineNewsObj>())
            savedItemList.add(OfflineNewsObj(article,html))
            Paper.book().write("items" , savedItemList)
            Toast.makeText(c,"Page Saved" , Toast.LENGTH_SHORT).show()
        }

    }
}
