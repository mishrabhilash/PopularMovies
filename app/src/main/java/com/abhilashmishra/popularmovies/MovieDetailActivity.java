package com.abhilashmishra.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity {

    private String posterThumbnailAddress;
    private String moviePlotSummary;
    private String releaseDate;
    private double rating;
    private String title;

    private TextView txtPlotSummary,txtReleaseDate,txtTitle,txtSubTitle;
    private ImageView imgView;
    String baseUrl = "http://image.tmdb.org/t/p/w185/";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        
        initVars();
        initUI();
    }

    private void initVars() {
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("jsonString"));
            posterThumbnailAddress = jsonObject.optString("poster_path");
            moviePlotSummary = jsonObject.optString("overview");
            releaseDate = jsonObject.optString("release_date");
            title = jsonObject.optString("title");
            rating = jsonObject.optDouble("vote_average");
        }catch (JSONException ex){
            ex.printStackTrace();
        }

    }

    private void initUI() {
        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtPlotSummary = (TextView)findViewById(R.id.text_view_plot_summary);
        txtPlotSummary.setText(moviePlotSummary);

        txtReleaseDate = (TextView)findViewById(R.id.text_view_release_date);
        txtReleaseDate.setText(releaseDate);

        txtTitle = (TextView)findViewById(R.id.movie_title);
        txtTitle.setText(title);
        txtTitle.setSelected(true);

        txtSubTitle = (TextView)findViewById(R.id.movie_rating);
        txtSubTitle.setText(getResources().getString(R.string.user_rating)+": "+rating+"/10");

        imgView = (ImageView)findViewById(R.id.imageView) ;
        Picasso.with(MovieDetailActivity.this).load(baseUrl+posterThumbnailAddress).into(imgView);
    }

}
