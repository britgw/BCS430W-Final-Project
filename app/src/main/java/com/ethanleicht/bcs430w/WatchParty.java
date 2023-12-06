package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WatchParty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_party);

        String movie = getIntent().getStringExtra("movie");

        WebView webView = findViewById(R.id.watchTogether);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDisplayZoomControls(true);

        // Stop redirecting to chrome
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        screenWidth /= 3;
        screenHeight /= 3;

        String frameVideo = "<html><body>" +
                "<iframe width="+screenWidth+" height="+screenHeight +
                " src=\"https://www.youtube.com/embed/"+movie+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        //webView.loadData(frameVideo, "text/html", "utf-8");
        webView.loadUrl("http://108.14.0.126/BCS430W/Event.php?watchpartyid=37");
    }
}