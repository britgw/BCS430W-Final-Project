package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.sql.ResultSet;

public class MovieList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.movieListToolbar);
        myToolbar.setTitle("Movie List");
        setSupportActionBar(myToolbar);

        // Setup webview
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        // Stop redirecting to chrome
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });
        // go to our website
        webView.loadUrl("http://70.107.82.4/BCS430w/Home.html");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        if(item.getItemId() == R.id.movieSearch) {
            Intent movieList = new Intent(getApplicationContext(), SearchMovies.class);
            startActivity(movieList);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}