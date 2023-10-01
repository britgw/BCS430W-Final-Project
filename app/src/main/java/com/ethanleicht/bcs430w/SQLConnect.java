package com.ethanleicht.bcs430w;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnect {
    static Connection con;

    // Connects to the SQL Server
    public Connection getCon() {
        // Connect to SQL server
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionUrl = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            // TODO: Fix this. The application stops responding when it tries to get a connection.
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://70.107.82.4:33062/test", "root", "shelly");
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
            Log.e("SQL", "Unable to connect");
        }
        //con = connection;
        return connection;
    }

    // Query SQL server
    public static ResultSet getResultsFromSQL(String query){
        String result = "";
        try{
            SQLConnect connectionClass = new SQLConnect();
            con = connectionClass.getCon();
            if(con != null){
                // String query = "SELECT * FROM MOVIES";
                Statement st = con.createStatement();
                return st.executeQuery(query);
            } else {
                Log.e("SQL", "No connection");
            }
        }catch (Exception e){
            Log.e("SQL", e.getMessage());
        }
        return null;
    }
}
