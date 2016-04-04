package com.abhilashmishra.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mishrabhilash on 4/4/16.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {
    static ArrayList<JSONObject> optionList;
    static Context context;
    String baseUrl = "http://image.tmdb.org/t/p/w185/";

    public MoviePosterAdapter(Context ctx,ArrayList<JSONObject>oList){
        context=ctx;
        optionList=oList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_movie_poster, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(baseUrl+optionList.get(position).optString("poster_path")).into(holder.item);
    }

    @Override
    public int getItemCount() {
        return optionList!=null?optionList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView item;
        public ViewHolder(View itemView) {
            super(itemView);
            item=(ImageView) itemView.findViewById(R.id.movie_poster_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,MovieDetailActivity.class);
            intent.putExtra("jsonString",optionList.get(getAdapterPosition()).toString());
            context.startActivity(intent);
        }
    }
}