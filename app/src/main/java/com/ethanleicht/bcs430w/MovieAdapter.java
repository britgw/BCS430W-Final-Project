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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movieSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView desc;
        private ImageView img;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleView);
            desc = (TextView) view.findViewById(R.id.descView);
            img = (ImageView) view.findViewById(R.id.imageView);

        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDesc() {
            return desc;
        }

        public ImageView getImage() {
            return img;
        }
    }

    private NameAdapterListener listener;

    void setOnItemClickListener(NameAdapterListener _listener)
    {
        listener = _listener;
    }

    interface NameAdapterListener {
        void onClick(int position);
    }

    public MovieAdapter(List<Movie> dataSet) {
        movieSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTitle().setText(movieSet.get(position).getTitle());
        viewHolder.getDesc().setText(movieSet.get(position).getDesc());
        try {
            InputStream is = (InputStream) new URL(movieSet.get(position).getImg()).getContent();
            Drawable d = Drawable.createFromStream(is, movieSet.get(position).getTitle());
            viewHolder.getImage().setImageDrawable(d);
        }catch (Exception e){
            Log.e("MOVIEDB", "INVALID IMAGE");
            Log.e("MOVIEDB", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return movieSet.size();
    }

    public void setData(List<Movie> list)
    {
        movieSet = list;
        notifyDataSetChanged();
    }

}
