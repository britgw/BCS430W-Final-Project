package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchMovies extends AppCompatActivity {
    private final String API_KEY = "api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String POPULAR_URL = BASE_URL + "/discover/movie?sort_by=popularity.desc&"+API_KEY;
    private final String TRENDING_URL = BASE_URL + "/trending/all/day?"+API_KEY;
    private final String TOPRATED_URL = BASE_URL + "/movie/top_rated?"+API_KEY;
    private final String NOWPLAYING_URL = BASE_URL + "/movie/now_playing?"+API_KEY;
    private final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private final String SEARCH_URL = BASE_URL + "/search/movie?"+API_KEY+"&query=";

    private void updateSearchResults(){
        TextView searchBar = findViewById(R.id.searchBar);
        RecyclerView searchResults = findViewById(R.id.searchResults);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            glm = new GridLayoutManager(this, 6);
        }
        searchResults.setLayoutManager(glm);
        MovieAdapter movieAdapter = new MovieAdapter(new ArrayList<Movie>());
        searchResults.setAdapter(movieAdapter);
        if(searchBar.getText().toString().equals("")){
            return;
        }
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Get Movies from search query
            JSONObject movies = Movie.readJsonFromUrl(SEARCH_URL +searchBar.getText().toString());
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
            searchResults.setAdapter(movieAdapter);
        }catch(Exception e){
            Log.e("MOVIEDB", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);

        TextView list = findViewById(R.id.list);
        TextView searchBar = findViewById(R.id.searchBar);

        list.setText("");
        searchBar.setText("");

        searchBar.setOnKeyListener((l, k, j) -> {
            updateSearchResults();
            return false;
        });
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateSearchResults();
    }
}