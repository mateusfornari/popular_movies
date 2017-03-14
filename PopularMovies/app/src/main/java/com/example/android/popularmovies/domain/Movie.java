package com.example.android.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mateus on 13/03/17.
 */

public class Movie implements Parcelable{
    private int id;
    private String title;
    private String synopsis;
    private float rating;
    private String releaseDate;

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        synopsis = in.readString();
        rating = in.readFloat();
        releaseDate = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeFloat(rating);
        dest.writeString(releaseDate);
    }
}
