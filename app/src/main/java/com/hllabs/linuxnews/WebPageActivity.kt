package com.hllabs.linuxnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_page.*

class WebPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        if(intent != null) {
            val article = intent.getParcelableExtra<NewsArticle>("i")
            if(article != null) {
                webview.settings.javaScriptEnabled = true
                webview.webChromeClient = WebChromeClient()
                webview.webViewClient = WebViewClient()
                webview.loadUrl(article.link)

            }
        }
    }
}
