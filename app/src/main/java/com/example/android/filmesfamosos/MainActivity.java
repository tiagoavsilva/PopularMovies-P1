package com.example.android.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.filmesfamosos.utilities.NetworkUtils;
import com.example.android.filmesfamosos.utilities.OpenMoviesJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    //Default load mode is most popular
    private static int searchMode = 1;

    private static final int numOfColumnsPortrait = 2;
    private static final int numOfColumnsLandscape = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int orientation = this.getResources().getConfiguration().orientation;
        Log.i(TAG, "Orientation: "+orientation);

        GridLayoutManager layoutManager;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutManager = new GridLayoutManager(this, numOfColumnsPortrait);
        else
            layoutManager = new GridLayoutManager(this, numOfColumnsLandscape);
        mRecyclerView.setLayoutManager(layoutManager);

        /*
        * Use this setting to improve performance if you know that changes in content do not
        * change the child layout size in the RecyclerView
        */
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        Log.i(TAG, "searchMode: "+searchMode);
        checkNetworkConnection(this);
    }

    private void checkNetworkConnection(final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            Log.e(TAG, "is connected");
            loadMoviesData(searchMode);
        }
        else{
            Log.e(TAG, "is disconnected");
            FrameLayout MainCoordinatorLayout =  (FrameLayout) findViewById(R.id.coordinatorLayout);
            Snackbar snackbar =
                    Snackbar.make(MainCoordinatorLayout, getString(R.string.withoutInternet),
                            Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetworkConnection(context);
                }
            });
            showErrorMessage();
            snackbar.show();
        }
    }

    private void loadMoviesData(int mode){
        showMovieDataView();
        new FetchMoviesTask().execute(mode);
    }

    @Override
    public void onClick(Movie movie) {
        Log.i(TAG, "Movie: " + movie);
        Intent it = new Intent(this,MovieDetailsActivity.class);
        it.putExtra("movie",movie);
        startActivity(it);
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private class FetchMoviesTask extends AsyncTask<Integer,Void,Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(Integer... params) {

            URL moviesRequestUrl = NetworkUtils.buildUrl(params[0], getApplicationContext());

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                Log.i(TAG, "JSON response: " + jsonMoviesResponse);
                 return OpenMoviesJsonUtils.getMoviesFromJson(MainActivity.this, jsonMoviesResponse);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMovieDataView();
                Log.i(TAG, "Number of movies: " + moviesData.length);
                mMoviesAdapter.setMovieData(moviesData);
            }
            else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.options, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular) {
            searchMode = 1;
            checkNetworkConnection(this);
            return true;
        }

        if (id == R.id.highest_rated) {
            searchMode = 2;
            checkNetworkConnection(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
