package com.example.android.filmesfamosos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntent().getExtras().getParcelable("movie");

        String posterUrl = getString(R.string.poster_url);
        ImageView imageView = (ImageView) findViewById(R.id.poster_mini);

        String posterPath = movie.getPoster_path();
        if(posterPath != null)
            Picasso.with(this).load(posterUrl+posterPath).into(imageView);

        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());

        TextView movieYear = (TextView) findViewById(R.id.movie_year);
        movieYear.setText(movie.getRelease_date());

        TextView movieVote = (TextView) findViewById(R.id.movie_vote_average);
        String text = movie.getVote_average()+"/10";
        movieVote.setText(text);

        TextView movieOverview = (TextView) findViewById(R.id.movie_overview);
        movieOverview.setText(movie.getOverview());

    }
}
