package com.abhilashmishra.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private CharSequence[] choices;
    private RecyclerView moviePosterView;
    private MoviePosterAdapter adapter;
    private ArrayList<JSONObject> moviePosterList;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        initUi();
    }

    private void initVars() {
        choices=getResources().getStringArray(R.array.choices);
        moviePosterList = new ArrayList<>();
        adapter = new MoviePosterAdapter(MainActivity.this,moviePosterList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
    }

    private void initUi() {
        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Taken help from http://stackoverflow.com/a/28348732/2953413
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_title,R.id.text_title,choices);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.choices,R.layout.spinner_title);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        Spinner mNavigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        mNavigationSpinner.setAdapter(arrayAdapter);
        toolbar.addView(mNavigationSpinner);

        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(((TextView)view).getText().equals(choices[0])){
                    new FetchMovieDetailsTask().execute("popular");
                }else if(((TextView)view).getText().equals(choices[1])){
                    new FetchMovieDetailsTask().execute("top_rated");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        moviePosterView = (RecyclerView)findViewById(R.id.movie_grid);
        moviePosterView.setAdapter(adapter);
        moviePosterView.setLayoutManager(gridLayoutManager);
    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, String> {


        private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String resultJson = null;

            try {
                String baseUrl = "http://api.themoviedb.org/3/movie/"+params[0]+"?";
                String apiKey = "api_key=" + BuildConfig.API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                resultJson = buffer.toString();
                return resultJson;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.optJSONArray("results");
                moviePosterList.clear();
                for(int i=0;i<jsonArray.length();i++){
                    moviePosterList.add(jsonArray.getJSONObject(i));
                }
                adapter.notifyDataSetChanged();



            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }

    }
}
