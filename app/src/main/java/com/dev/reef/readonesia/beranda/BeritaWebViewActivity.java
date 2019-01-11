package com.dev.reef.readonesia.beranda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dev.reef.readonesia.R;

public class BeritaWebViewActivity extends AppCompatActivity  {
    private String url;
    private WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_web_view);

        browser = (WebView) findViewById(R.id.webview);
        browser.setWebViewClient(new MyBrowser());

        try {
            url = getIntent().getStringExtra("url");
        } catch (Exception e) {
            url = "http://www.google.com";
        }

        browser.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
