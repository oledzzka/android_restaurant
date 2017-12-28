package com.example.oleg.restaurants.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.oleg.restaurants.R;

/**
 * Created by oleg on 23.12.17.
 */

public class WebActivity extends AppCompatActivity {

    public final static String GET_URL = "GET_URL";

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = null;
        if (savedInstanceState != null ) {
            url = savedInstanceState.getString(GET_URL);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) url = bundle.getString(GET_URL);
        }
        if ( url == null ) {
            finish();
            return;
        }
        setContentView(R.layout.activity_webview);

        WebView mWebView = (WebView) findViewById(R.id.web_view);
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        mWebView.loadUrl(url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
