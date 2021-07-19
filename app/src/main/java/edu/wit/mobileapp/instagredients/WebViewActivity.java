package edu.wit.mobileapp.instagredients;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle bundle = this.getIntent().getExtras();
        String link = bundle.getString("link");

        Log.v("myApp", "link = " + link);

        webView = (WebView) findViewById(R.id.webView);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // Makes it so that it doesn't open chrome browser
        webView.setWebViewClient(new WebViewClient());

        // Zoom out the page to fit the display
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // Provide pinch zoom operation
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.loadUrl(link);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}