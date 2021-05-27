package com.tubianto.webviewapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var loading: ProgressBar
    private lateinit var url: String
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFullscreen()
        init()
        setupUI()
    }

    private fun setFullscreen(){
        //Menyembunyikan action bar
        supportActionBar?.hide()
        //Mengatur layout menjadi Full Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun init(){
        url = "https://tubianto.com/"
        loading = findViewById<ProgressBar>(R.id.pb_loading)
        webView = findViewById<WebView>(R.id.wv_promo)
    }

    private fun setupUI(){
        if (isOnline(this)){
            loadWebview()
        } else {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ServiceCast")
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    private fun loadWebview(){
        webView.webViewClient = myWebclient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }

    inner class myWebclient: WebViewClient() {
        override fun onPageFinished(view:WebView, url:String) {
            super.onPageFinished(view, url)
            loading.visibility = View.GONE
        }

        override fun shouldOverrideUrlLoading(view:WebView, url: String):Boolean {
            view.loadUrl(url)
            return true
        }
    }

    override fun onKeyDown(keyCode:Int, event: KeyEvent):Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}