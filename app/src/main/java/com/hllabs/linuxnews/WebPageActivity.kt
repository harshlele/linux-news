package com.hllabs.linuxnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_page.*




/*
* Activity to show the article content
* */
class WebPageActivity : AppCompatActivity() {

    var isFullPageLoaded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        //get the intent
        if(intent != null) {
            val article = intent.getParcelableExtra<NewsArticle>("i")
            webview.settings.javaScriptEnabled = true
            webview.webChromeClient = WebChromeClient()

            webview.addJavascriptInterface(JSInterface(), "HTMLOUT")

            webview.webViewClient = object:WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    if(isFullPageLoaded) btnDownloadPage.visibility = View.VISIBLE
                }
            }

            btnDownloadPage.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    /* This call inject JavaScript into the page which just finished loading. */
                    webview.loadUrl("javascript:window.HTMLOUT.processHTML(document.documentElement.outerHTML);")
                }
            })


            if(article != null) {
                //if the article content is not available,just load the full site
                if(article.content == "") {
                    webview.loadUrl(article.link)
                }
                //if it is, load just the description and title
                else{
                    isFullPageLoaded = false
                    btnLoadFullPage.visibility = View.VISIBLE
                    loadContent(article)
                }

            }

            btnLoadFullPage.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    webview.loadUrl(article.link)
                    btnLoadFullPage.visibility = View.GONE
                }

            })
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


    class JSInterface{
        @JavascriptInterface
        fun processHTML(html: String) {


        }
    }
}
