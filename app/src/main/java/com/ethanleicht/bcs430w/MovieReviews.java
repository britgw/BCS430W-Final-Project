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

import java.sql.ResultSet;
import java.util.ArrayList;

public class MovieReviews extends AppCompatActivity {
    private final String API_KEY = "api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private final String SEARCH_URL = BASE_URL + "/search/movie?"+API_KEY+"&query=";
    private void updateSearchResults(){
        TextView searchBar = findViewById(R.id.reviewSearchBar);
        RecyclerView searchResults = findViewById(R.id.reviewRecyclerView);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            glm = new GridLayoutManager(this, 6);
        }
        searchResults.setLayoutManager(glm);
        MovieAdapter movieAdapter = new MovieAdapter(new ArrayList<Movie>());
        searchResults.setAdapter(movieAdapter);
        try{
            // Get Movies from search query
            String query = "SELECT * FROM moviereview " +
                    "JOIN users ON moviereview.userid = users.userid ";
            String searchQuery = searchBar.getText().toString();
            //if(!searchBar.getText().toString().equals(""))
                //query += "WHERE users.username LIKE '%"+searchBar.getText().toString()+"%' " +
                //        "AND privacy = 'Public'";

            SQLConnect con = new SQLConnect();
            ResultSet result = con.getResultsFromSQL(query);
            ArrayList<Movie> movieResults = new ArrayList<Movie>();
            while(result.next()){
                // Get the movie at index i
                String movieid = result.getString("moviereview.movieid");
                if(!movieid.equals("0")) {
                    Movie m = Movie.getMovieById(result.getString("moviereview.movieid"));
                    // Get Username
                    String user = result.getString("users.privacy").equals("Public") ?
                            result.getString("users.username") : "Unknown";
                    // Make movie object
                    String id = m.getId();
                    String title = m.getTitle();
                    String desc = user + ": " +
                            result.getInt("moviereview.Rating") + "/5: " +
                            result.getString("moviereview.Description");
                    String img_url = IMG_URL + m.getImg();
                    Movie movie = new Movie(id, title, desc, img_url);
                    movieResults.add(movie);
                }
            }
            con.closeConnection();
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
        setContentView(R.layout.activity_movie_reviews);

        updateSearchResults();

        TextView searchBar = findViewById(R.id.reviewSearchBar);
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