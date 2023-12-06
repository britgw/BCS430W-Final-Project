package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FriendList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        int user = getIntent().getIntExtra("userid", 0);

        RecyclerView friendList = findViewById(R.id.friendListView);
        friendList.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter userAdapter = new UserAdapter(new ArrayList<String>(),new ArrayList<String>());
        friendList.setAdapter(userAdapter);

        try{
            // Get username and other user info
            ArrayList<String> usernames = new ArrayList<String>();
            ArrayList<String> pfps = new ArrayList<String>();
            String query = "SELECT friend.username AS 'friend', friend.pfp AS pic, friend.userid AS friendid " +
                    "FROM users AS u " +
                    "JOIN friendship AS fs ON u.userid = fs.user1 " +
                    "JOIN users AS friend ON friend.userid = fs.user2 " +
                    "WHERE u.userid = " + user;
            SQLConnect con = new SQLConnect();
            ResultSet result = con.getResultsFromSQL(query);
            if(result.first()) {
                usernames.add(result.getString("friend"));
                // TODO: Add friend profile pictures from blob
                if(result.getBlob("pic") != null) {
                    String imageUrl = result.getString("friendid");
                    pfps.add(imageUrl);
                }
                while (result.next()) {
                    usernames.add(result.getString(1));
                    if(result.getBlob("pic") != null) {
                        String imageUrl = result.getString("friendid");
                        pfps.add(imageUrl);
                    }
                }
            }
            con.closeConnection();
            userAdapter.setData(usernames, pfps);
            userAdapter.setOnItemClickListener(position -> {
                try {
                    String getquery = "SELECT userid FROM users " +
                            "WHERE username = '" + usernames.get(position) + "';";
                    ResultSet getresult = con.getResultsFromSQL(getquery);
                    if (getresult.first()) {
                        int userid = getresult.getInt("userid");
                        con.closeConnection();
                        Intent friend = new Intent(getApplicationContext(), UserDetails.class);
                        friend.putExtra("friendid", userid);
                        int currentUser = getIntent().getIntExtra("userid", 0);
                        friend.putExtra("userid", currentUser);
                        startActivity(friend);
                    }
                }catch(Exception e){
                    Log.e("UserDB", e.toString());
                }
            });
            friendList.setAdapter(userAdapter);

        }catch (Exception e){
            Log.e("MOVIEDB", "Invalid user");
            Log.e("MOVIEDB", e.toString());
        }
    }
}