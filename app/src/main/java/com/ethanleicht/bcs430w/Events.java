package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Events extends AppCompatActivity {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Button addEvent = findViewById(R.id.createEvent);
        TextView eventLabel = findViewById(R.id.eventsLabel);
        TextView rsvpLabel = findViewById(R.id.rsvpLabel);
        RecyclerView events = findViewById(R.id.events);
        RecyclerView rsvps = findViewById(R.id.rsvps);

        int user = getIntent().getIntExtra("userid", 0);

        addEvent.setOnClickListener(l -> {
            Intent eventIntent = new Intent(getApplicationContext(), CreateEvent.class);
            eventIntent.putExtra("userid", user);
            startActivity(eventIntent);
        });
        // Events
        GridLayoutManager eventGlm = new GridLayoutManager(this, 1);
        eventGlm.setOrientation(RecyclerView.HORIZONTAL);
        MovieAdapter eventAdapter = new MovieAdapter(new ArrayList<>());
        events.setLayoutManager(eventGlm);
        events.setAdapter(eventAdapter);
        try{
            String query = "SELECT w.*, u.* FROM watchparty as w " +
                    "JOIN rsvp on rsvp.watchparty = w.watchpartyid " +
                    "JOIN users as u on rsvp.user = u.userid " +
                    "WHERE u.userid = " + user + " AND " +
                    "w.creator != " + user +
                    " UNION ALL " +
                    "SELECT w.*, u.* FROM watchparty as w " +
                    "JOIN users as u on w.creator = u.userid " +
                    "WHERE w.creator = " + user;
            SQLConnect con = new SQLConnect();
            ResultSet result = con.getResultsFromSQL(query);
            ArrayList<Movie> eventResults = new ArrayList<Movie>();
            while(result.next()){
                String id = result.getString("watchpartyid");
                String title = result.getString("movie");
                String desc = result.getString("username");
                String img_url = "http://108.14.0.126/BCS430W/" +
                        result.getString("creator") + ".png";
                Movie m = new Movie(id, title, desc, img_url);
                eventResults.add(m);
            }
            con.closeConnection();
            eventAdapter.setData(eventResults);
            eventAdapter.setOnItemClickListener(position -> {
                String eventid = eventResults.get(position).getId();
                try{
                    SQLConnect con2 = new SQLConnect();
                    ResultSet res = con2.getResultsFromSQL("SELECT * FROM watchparty " +
                            "WHERE watchpartyid = " + eventid);
                    String response = "error";
                    if(res.first()) {
                        SQLConnect timeCon = new SQLConnect();
                        ResultSet timeRes = timeCon.getResultsFromSQL("SELECT TIMESTAMPDIFF(SECOND, NOW()," +
                                "(SELECT TIME FROM WATCHPARTY WHERE watchpartyid = " + eventid + "))");
                        boolean start = false;
                        if(timeRes.first()){
                            start = Integer.parseInt(timeRes.getString(1)) < 300;
                        }
                        if(start){
                            HashMap<String, String> movieList = new HashMap<>();
                            movieList.put("Die Hard", "mefDtBXEG-4");
                            movieList.put("Ghostbusters", "dj2nqC1THzk");
                            movieList.put("Twilight", "zZUUHthMeA4");
                            movieList.put("Kung Fu Panda", "2BWg-WwCaCg");
                            response = "Starts at " + res.getString("time");

                            Intent wpIntent = new Intent(getApplicationContext(), WatchParty.class);
                            int currentUser = getIntent().getIntExtra("userid", 0);
                            wpIntent.putExtra("userid", currentUser);
                            wpIntent.putExtra("movie", eventResults.get(position).getTitle());
                            startActivity(wpIntent);
                        }else {
                            response = "Starts at " + res.getString("time");
                        }
                        timeCon.closeConnection();
                    }
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    con2.closeConnection();
                }catch (Exception e){
                    Log.e("SQL", e.toString());
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            });
            events.setAdapter(eventAdapter);
        }catch(Exception e){
            Log.e("MOVIEDB", e.toString());
        }
        // RSVPs
        GridLayoutManager rsvpGlm = new GridLayoutManager(this, 1);
        rsvpGlm.setOrientation(RecyclerView.HORIZONTAL);
        MovieAdapter rsvpAdapter = new MovieAdapter(new ArrayList<Movie>());
        rsvps.setLayoutManager(rsvpGlm);
        rsvps.setAdapter(rsvpAdapter);
        try{
            String query = "SELECT w.*, u.* FROM watchparty as w " +
                    "JOIN users as u on w.creator = u.userid " +
                    "WHERE w.creator != " + user;
            SQLConnect con1 = new SQLConnect();
            ResultSet result1 = con1.getResultsFromSQL(query);
            ArrayList<Movie> rsvpResults = new ArrayList<Movie>();
            while(result1.next()){
                String id = result1.getString("watchpartyid");
                String title = result1.getString("movie");
                String desc = result1.getString("username");
                String img_url = "http://108.14.0.126/BCS430W/" +
                        result1.getString("userid") + ".png";
                Movie m = new Movie(id, title, desc, img_url);
                rsvpResults.add(m);
            }
            con1.closeConnection();

            rsvpAdapter.setData(rsvpResults);
            rsvpAdapter.setOnItemClickListener(position -> {
                String eventid = rsvpResults.get(position).getId();
                try{
                    SQLConnect con2 = new SQLConnect();
                    ResultSet res = con2.getResultsFromSQL("SELECT * FROM rsvp " +
                            "JOIN watchparty as w on rsvp.watchparty = w.watchpartyid " +
                            "JOIN users as u on w.creator = u.userid " +
                            "WHERE rsvp.user = " + user + " AND " +
                            "watchparty = " + eventid);
                    String response;
                    if(res.first()) {
                        SQLConnect con3 = new SQLConnect("DELETE FROM rsvp " +
                                "WHERE user = " + user + " AND  watchparty = " + eventid);
                        response = "RSVP Removed";
                    }else{
                        SQLConnect con3 = new SQLConnect("INSERT IGNORE INTO rsvp " +
                                "(watchparty, user) VALUES (" + eventid + ", " + user + ")");
                        response = "RSVP Added";
                    }
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    con2.closeConnection();
                }catch (Exception e){
                    Log.e("SQL", e.toString());
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            });
            rsvps.setAdapter(rsvpAdapter);
        }catch(Exception e){
            Log.e("MOVIEDB", e.toString());
        }
    }
}