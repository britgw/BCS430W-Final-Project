package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchMovies extends AppCompatActivity {

    private String API_KEY = "api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private String BASE_URL = "https://api.themoviedb.org/3";
    private String POPULAR_URL = BASE_URL + "/discover/movie?sort_by=popularity.desc&"+API_KEY;
    private String TRENDING_URL = BASE_URL + "/trending/all/day?"+API_KEY;
    private String TOPRATED_URL = BASE_URL + "/movie/top_rated?"+API_KEY;
    private String NOWPLAYING_URL = BASE_URL + "/movie/now_playing?"+API_KEY;
    private String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private String searchURL = BASE_URL + "/search/movie?"+API_KEY+"&query=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);

        TextView list = findViewById(R.id.list);
        TextView searchBar = findViewById(R.id.searchBar);
        RecyclerView searchResults = findViewById(R.id.searchResults);

        list.setText("");

        searchBar.setOnKeyListener((l, k, j) -> {
            if(searchBar.getText().toString() == ""){
                list.setText("");
                return false;
            }
            try{
                // Get Movies from search query
                JSONObject movies = Movie.readJsonFromUrl(searchURL+searchBar.getText().toString());
                JSONArray movieList = movies.getJSONArray("results");
                String output = "";
                ArrayList<Movie> movieResults = new ArrayList<Movie>();
                for(int i = 0;i < movieList.length();i++){
                    // Get the movie at index i
                    JSONObject movie = movieList.getJSONObject(i);
                    // Make movie object
                    String title = movie.getString("title");
                    String desc = movie.getString("overview");
                    String img_url = IMG_URL + movie.getString("backdrop_path");
                    Movie m = new Movie(title, desc, img_url);
                    // output title
                    output += movie.getString("title") + "\n";
                }
                searchResults.setLayoutManager(new LinearLayoutManager(this));
                MovieAdapter movieAdapter = new MovieAdapter(movieResults.toArray(new Movie[movieResults.size()]));
                searchResults.setAdapter(movieAdapter);
                list.setText(output);
            }catch(Exception e){
                Log.e("MOVIEDB", e.toString());
            }
            return false;
        });
    }
}