package com.example.b0501111.helloworld.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.b0501111.helloworld.R;
import com.example.b0501111.helloworld.app.MyApplication;
import com.example.b0501111.helloworld.helper.Movie;
import com.example.b0501111.helloworld.helper.SwipeListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = MainActivity.class.getSimpleName();

    private String URL_TOP_250 = "http://api.androidhive.info/json/imdb_top_250.php?offset=";

    private  SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private SwipeListAdapter mSwipeListAdapter;
    private List<Movie> mMovieList;

    //initially offset will be 0,
    private int offset = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mMovieList = new ArrayList<>();
        mSwipeListAdapter = new SwipeListAdapter(this, mMovieList);
        mListView.setAdapter(mSwipeListAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchMovies();
            }
        });
}
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
            fetchMovies();
    }

    /**
     * Fetching movies json by making http call
     */
    private  void fetchMovies()
    {
        // showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);
        // appending offset to url
        String url = URL_TOP_250 + offset;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject movieObj = response.getJSONObject(i);

                                    int rank = movieObj.getInt("rank");
                                    String title = movieObj.getString("title");

                                    Movie m = new Movie(rank, title);

                                    mMovieList.add(0, m);

                                    // Updating offset value to highest value
                                    if (rank >= offset)
                                        offset = rank;
                                    ;
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }
                            mSwipeListAdapter.notifyDataSetChanged();
                        }
                        // Stopping swipe refresh
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getmInstance().addToRequest(req);
    }
}
