package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
            String query = "SELECT friend.username AS 'friend', friend.pfp AS pic FROM users AS u " +
                    "JOIN friendship AS fs ON u.userid = fs.user1 " +
                    "JOIN users AS friend ON friend.userid = fs.user2 " +
                    "WHERE u.userid = " + user;
            ResultSet result = SQLConnect.getResultsFromSQL(query);
            if(result.first()) {
                usernames.add(result.getString("friend"));
                // TODO: Add friend profile pictures from blob
                if(result.getBlob("pic") != null)
                    pfps.add(result.getBlob("pic").toString());
                while (result.next()) {
                    usernames.add(result.getString(1));
                    //pfps.add(result.getBlob(2).toString());
                }
            }
            SQLConnect.closeConnection();
            userAdapter.setData(usernames, pfps);
            friendList.setAdapter(userAdapter);

        }catch (Exception e){
            Log.e("MOVIEDB", "Invalid user");
            Log.e("MOVIEDB", e.toString());
        }
    }
}