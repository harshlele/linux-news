package com.hllabs.linuxnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_page.*

class WebPageActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        if(intent != null) {
            val article = intent.getParcelableExtra<NewsArticle>("i")
            webview.settings.javaScriptEnabled = true
            webview.webChromeClient = WebChromeClient()
            webview.webViewClient = WebViewClient()
            if(article != null) {

                if(article.content == "") {
                    webview.loadUrl(article.link)
                }
                else{
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
}
