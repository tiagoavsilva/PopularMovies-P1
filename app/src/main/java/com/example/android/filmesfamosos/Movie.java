package com.example.android.filmesfamosos;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private final String title;
    private final String posterPath;
    private final String overview;
    private final String releaseDate;
    private final String voteAverage;

    public Movie(String title, String poster_path, String overview, String release_date, String vote_average) {
        this.title = title;
        this.posterPath = poster_path;
        this.overview = overview;
        this.releaseDate = release_date;
        this.voteAverage = vote_average;
    }

    private Movie(Parcel in) {
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return releaseDate;
    }

    public String getVote_average() {
        return voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", poster_path='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + releaseDate + '\'' +
                ", vote_average='" + voteAverage + '\'' +
                '}';
    }
}
