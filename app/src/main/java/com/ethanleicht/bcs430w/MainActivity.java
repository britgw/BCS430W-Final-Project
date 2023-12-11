package com.ethanleicht.bcs430w;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.crypt.JBCrypt.BCrypt;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null && intent.getExtras() != null)
                        getIntent().putExtras(intent.getExtras());
                }
            });

    public void scheduleNotification(Event e) {
        //delay is after how much time(in millis) from current time you want to schedule the notification
        long delay = 1000;
        try {
            SQLConnect timeCon = new SQLConnect();
            ResultSet timeRes = timeCon.getResultsFromSQL("SELECT TIMESTAMPDIFF(SECOND, NOW()," +
                    "(SELECT TIME FROM WATCHPARTY WHERE watchpartyid = " + e.getId() + "))");
            if (timeRes.first()) {
                delay = Integer.parseInt(timeRes.getString(1));
                delay = delay - 300;
                delay = delay * 1000;
            }

            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            CharSequence name = "MovieEvents";
            String description = "Events from Movie App";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("events", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // Open Event on click (Not working)
            Intent intent = new Intent(getApplicationContext(), Events.class);
            PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), e.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Format notification
            Notification notification = new Notification.Builder(getApplicationContext(), "events")
                    .setSmallIcon(android.R.drawable.presence_video_online)
                    .setContentTitle("Movie")
                    .setContentText(e.getMovie() + " starts in 5 minutes")
                    .setContentIntent(activity)
                    .build();

            // Create PendingIntent for when the alarm goes off
            Intent notificationIntent = new Intent(getApplicationContext(), EventReceiver.class);
            notificationIntent.putExtra("notification_id", e.getId());
            notificationIntent.putExtra("notification", notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), e.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Schedule alarms
            long futureInMillis = SystemClock.elapsedRealtime() + delay;
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if(alarmManager.canScheduleExactAlarms())
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
            }
        }catch(Exception exception){
            Log.e("SQL", exception.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Import buttons and text inputs
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        Button guestButton = (Button) findViewById(R.id.guestButton);
        TextView username = (TextView) findViewById(R.id.usernameInput);
        TextView password = (TextView) findViewById(R.id.passwordInput);

        guestButton.setOnClickListener(v -> {
            Intent movieList = new Intent(getApplicationContext(), MovieList.class);
            mStartForResult.launch(movieList);
        });
        registerButton.setOnClickListener(v -> {
            Intent registerView = new Intent(getApplicationContext(), AccountRegister.class);
            mStartForResult.launch(registerView);
        });

        loginButton.setOnClickListener(v -> {
            // Check login information
            if(!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                try{
                    // Validate username and password
                    SQLConnect conpw = new SQLConnect();
                    ResultSet respw = conpw.getResultsFromSQL("SELECT * FROM users WHERE username = '" +
                            username.getText().toString() + "'");
                    String checkUsername = "username = '" + username.getText().toString() + "'";
                    String checkPassword = " AND userid = " + password.getText().toString();
                    if(respw.first()) {
                        String hashedpw = respw.getString("password");
                        checkPassword = hashedpw;
                        checkPassword = BCrypt.checkpw(password.getText().toString(), respw.getString("password")) ? "true" : "false";
                    }
                    conpw.closeConnection();

                    String query = "SELECT * FROM users WHERE "+checkUsername+" AND "+checkPassword;
                    SQLConnect con = new SQLConnect();
                    ResultSet result = con.getResultsFromSQL(query);

                    if(result != null && result.first()){
                        int user = result.getInt("userid");
                        if(!NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()){
                            Toast.makeText(getApplicationContext(), "Notifications not enabled", Toast.LENGTH_SHORT).show();
                            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 0);
                        }else{
                            // Setup Notifications
                            query = "SELECT w.*, u.* FROM watchparty as w " +
                                    "JOIN rsvp on rsvp.watchparty = w.watchpartyid " +
                                    "JOIN users as u on rsvp.user = u.userid " +
                                    "WHERE u.userid = " + user + " AND " +
                                    "w.creator != " + user +
                                    " UNION ALL " +
                                    "SELECT w.*, u.* FROM watchparty as w " +
                                    "JOIN users as u on w.creator = u.userid " +
                                    "WHERE w.creator = " + user;
                            SQLConnect eventCon = new SQLConnect();
                            ResultSet eventResult = con.getResultsFromSQL(query);
                            ArrayList<Event> events = new ArrayList<Event>();
                            while (eventResult.next()) {
                                int eId = Integer.parseInt(eventResult.getString("watchpartyid"));
                                String eMovie = eventResult.getString("movie");
                                int eCreator = Integer.parseInt(eventResult.getString("creator"));
                                String eTime = eventResult.getString("time");
                                events.add(new Event(eId, eMovie, eTime, eCreator));
                            }
                            for (Event event : events) {
                                scheduleNotification(event);
                            }
                        }
                        // Go to MovieList page
                        Intent movieList = new Intent(getApplicationContext(), MovieList.class);
                        movieList.putExtra("userid", result.getInt("userid"));
                        con.closeConnection();
                        mStartForResult.launch(movieList);
                    }else{
                        con.closeConnection();
                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    Log.e("SQL", e.toString());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
            }
        });
    }
}