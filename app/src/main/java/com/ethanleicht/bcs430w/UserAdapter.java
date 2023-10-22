package com.ethanleicht.bcs430w;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<String> usernames;
    private List<String> pfps;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private ImageView pfp;

        public ViewHolder(View view) {
            super(view);
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

    private UserAdapterListener listener;

    void setOnItemClickListener(UserAdapterListener _listener)
    {
        listener = _listener;
    }

    interface UserAdapterListener {
        void onClick(int position);
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
            InputStream is = (InputStream) new URL(pfps.get(position)).getContent();
            Drawable d = Drawable.createFromStream(is, pfps.get(position));
            viewHolder.getPfp().setImageDrawable(d);
        }catch (Exception e){
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
