package com.ethanleicht.bcs430w;

import android.content.Context;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    private static final String API_KEY = "api_key=0470883e66467443d1d8ad73e3c4a2ed";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";

    private String id;
    private String title;
    private String desc;
    private String img_url;
    public Movie(String id, String title, String desc, String img_url){
        this.setId(id);
        this.setTitle(title);
        this.setDesc(desc);
        this.setImg(img_url);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img_url;
    }

    public void setImg(String img_url) {
        this.img_url = img_url;
    }

    //public String getTrailer(){ return BASE_URL + "/movie/" + id + "/videos?" + API_KEY;  }

    public static String getTrailer(String movieId, int width, int height){
        String json = "https://api.themoviedb.org/3/movie/"+movieId+"/videos?" + API_KEY;

        try {
            JSONObject movies = Movie.readJsonFromUrl(json);
            JSONArray results = movies.getJSONArray("results");
            String key = results.getJSONObject(results.length() - 1).getString("key");
            //<iframe width="1264" height="480" src="https://www.youtube.com/embed/jCuEBVbmPcA" title="You Wanted a TEMU Gaming Setup…You were wrong." frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
            String frameVideo = "<html><body><iframe width="+width+" height="+height+" src=\"https://www.youtube.com/embed/"+key+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            //String videoLink = "https://www.youtube.com/video/"+key;
            return frameVideo;
        }catch (Exception e){
            Log.e("MOVIEDB", e.toString());
        }
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public static Movie getMovieById(String movieid){
        try {
            String url = BASE_URL + "/movie/" + movieid + "?" + API_KEY;
            JSONObject movies = Movie.readJsonFromUrl(BASE_URL + "/movie/" + movieid + "?" + API_KEY);
            String id = movieid;
            String title = movies.getString("title");
            String desc = movies.getString("overview");
            String img_url = IMG_URL + movies.getString("backdrop_path");
            Movie m = new Movie(id, title, desc, img_url);
            return m;
        }catch (Exception e){
            Log.e("MOVIEDB", e.toString());
        }
        return null;
    }
}
