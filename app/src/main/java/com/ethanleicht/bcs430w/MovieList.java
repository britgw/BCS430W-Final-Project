package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;

public class MovieList extends AppCompatActivity {
    private final String API_KEY = "api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String POPULAR_URL = BASE_URL + "/discover/movie?sort_by=popularity.desc&"+API_KEY;
    private final String TRENDING_URL = BASE_URL + "/trending/all/day?"+API_KEY;
    private final String TOPRATED_URL = BASE_URL + "/movie/top_rated?"+API_KEY;
    private final String NOWPLAYING_URL = BASE_URL + "/movie/now_playing?"+API_KEY;
    private final String IMG_URL = "https://image.tmdb.org/t/p/w500";

    private String currentUrl = POPULAR_URL;

    private void updateSearchResults(){
        RecyclerView movieListView = findViewById(R.id.movieList);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            glm = new GridLayoutManager(this, 6);
        }
        movieListView.setLayoutManager(glm);
        MovieAdapter movieAdapter = new MovieAdapter(new ArrayList<Movie>());
        movieListView.setAdapter(movieAdapter);
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Get Movies from search query
            JSONObject movies = Movie.readJsonFromUrl(currentUrl);
            JSONArray movieList = movies.getJSONArray("results");
            String output = "";
            ArrayList<Movie> movieResults = new ArrayList<Movie>();
            for(int i = 0;i < movieList.length();i++){
                // Get the movie at index i
                JSONObject movie = movieList.getJSONObject(i);
                // Make movie object
                String id = movie.getString("id");
                String title = movie.getString("title");
                String desc = movie.getString("overview");
                String img_url = IMG_URL + movie.getString("backdrop_path");
                Movie m = new Movie(id, title, desc, img_url);
                movieResults.add(m);
                // output title
                output += movie.getString("title") + "\n";
            }
            movieAdapter.setData(movieResults);
            movieAdapter.setOnItemClickListener(position -> {
                Movie m = movieResults.get(position);

                Intent movieDetails = new Intent(getApplicationContext(), MovieDetails.class);
                movieDetails.putExtra("movieid", m.getId());
                startActivity(movieDetails);
            });
            movieListView.setAdapter(movieAdapter);
        }catch(Exception e){
            Log.e("MOVIEDB", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.movieListToolbar);
        myToolbar.setTitle("Movie List");
        setSupportActionBar(myToolbar);

        updateSearchResults();

        Button popularButton = findViewById(R.id.popularButton);
        Button trendingButton = findViewById(R.id.trendingButton);
        Button topRatedButton = findViewById(R.id.topRatedButton);
        Button nowPlayingButton = findViewById(R.id.nowPlayingButton);

        popularButton.setOnClickListener(v -> {
            currentUrl = POPULAR_URL;
            updateSearchResults();
        });
        trendingButton.setOnClickListener(v -> {
            currentUrl = TRENDING_URL;
            updateSearchResults();
        });
        topRatedButton.setOnClickListener(v -> {
            currentUrl = TOPRATED_URL;
            updateSearchResults();
        });
        nowPlayingButton.setOnClickListener(v -> {
            currentUrl = NOWPLAYING_URL;
            updateSearchResults();
        });
        /*
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
        webView.loadUrl("http://108.14.0.126/BCS430w/Home.html");
         */
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateSearchResults();
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
            int user = getIntent().getIntExtra("userid", 0);
            movieList.putExtra("userid", user);
            startActivity(movieList);
            return true;
        }else if(item.getItemId() == R.id.profile) {
            Intent profile = new Intent(getApplicationContext(), AccountView.class); // change to register
            if(getIntent().getIntExtra("userid", 0) != 0) {
                profile = new Intent(getApplicationContext(), AccountView.class);
            }
            int user = getIntent().getIntExtra("userid", 0);
            profile.putExtra("userid", user);
            startActivity(profile);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}