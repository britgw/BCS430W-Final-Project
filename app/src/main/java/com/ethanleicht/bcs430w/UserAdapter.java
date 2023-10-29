package com.ethanleicht.bcs430w;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<String> usernames;
    private List<String> pfps;

    private static UserAdapterListener listener;

    void setOnItemClickListener(UserAdapterListener _listener)
    {
        listener = _listener;
    }

    interface UserAdapterListener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private ImageView pfp;

        public ViewHolder(View view) {
            super(view);


            view.setOnClickListener( v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onClick(position);
                }
            });

            username = (TextView) view.findViewById(R.id.usernameView);
            pfp = (ImageView) view.findViewById(R.id.pfpView);
        }

        public TextView getUsername() {
            return username;
        }
        public ImageView getPfp() {
            return pfp;
        }
    }

    public UserAdapter(List<String> usernameSet, List<String> pfpSet) {
        usernames = usernameSet;
        pfps = pfpSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getUsername().setText(usernames.get(position));
        try {
            URL urlConnection = new URL("http://108.14.0.126/BCS430w/"+ pfps.get(position) + ".png");
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            viewHolder.getPfp().setImageBitmap(bmp);
        }catch (Exception e){
            try {
                URL urlConnection = new URL("http://108.14.0.126/BCS430w/"+ pfps.get(position) + ".jpg");
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(input);
                viewHolder.getPfp().setImageBitmap(bmp);
            }catch (Exception ex){
                Log.e("MOVIEDB", "INVALID IMAGE");
                Log.e("MOVIEDB", ex.toString());
            }
            Log.e("MOVIEDB", "INVALID IMAGE");
            Log.e("MOVIEDB", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public void setData(List<String> userList, List<String> pfpList)
    {
        usernames = userList;
        pfps = pfpList;
        notifyDataSetChanged();
    }
}
