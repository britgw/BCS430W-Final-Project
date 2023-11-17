package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.sql.ResultSet;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Button watchlistButton = findViewById(R.id.addToWatchlist);
        watchlistButton.setOnClickListener(l -> {
            String query = "INSERT IGNORE INTO watchlist (userid, movie) VALUES (" +
                    getIntent().getIntExtra("userid", 0) + ", " +
                    getIntent().getStringExtra("movieid") + ")";
            SQLConnect con = new SQLConnect(query);
            con.closeConnection();
        });
        Button reviewButton = findViewById(R.id.writeReview);
        reviewButton.setOnClickListener(l -> {
            Intent ReviewIntent = new Intent(getApplicationContext(), MovieReviewAdd.class);
            int user = getIntent().getIntExtra("userid", 0);
            ReviewIntent.putExtra("userid", user);
            String movie = getIntent().getStringExtra("movieid");
            ReviewIntent.putExtra("movieid", movie);
            startActivity(ReviewIntent);
        });


        // Setup webview
        WebView webView = findViewById(R.id.TrailerBox);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        // Stop redirecting to chrome
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        screenWidth /= 3;
        screenHeight /= 3;

        // go to our website
        //webView.loadUrl("http://108.14.0.126/BCS430w/Home.html");
        String movieID = getIntent().getStringExtra("movieid");
        webView.loadData(Movie.getTrailer(movieID, screenWidth, screenHeight), "text/html", "utf-8");

    }
}