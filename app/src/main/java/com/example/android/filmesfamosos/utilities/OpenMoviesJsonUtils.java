/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.filmesfamosos.utilities;

import android.content.Context;

import com.example.android.filmesfamosos.Movie;
import com.example.android.filmesfamosos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle OpenMoviesMap JSON data.
 */
public final class OpenMoviesJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Movies.
     *
     * @param forecastJsonStr JSON response from server
     *
     * @return Array of Movie describing movies data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getMoviesFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String RESULTS = context.getString(R.string.JSON_tag_results);
        final String TITLE = context.getString(R.string.JSON_tag_title);
        final String POSTER_PATH = context.getString(R.string.JSON_tag_poster_path);
        final String OVERVIEW = context.getString(R.string.JSON_tag_overview);
        final String RELEASE_DATE = context.getString(R.string.JSON_tag_release_date);
        final String VOTE_AVERAGE = context.getString(R.string.JSON_tag_vote_average);

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray moviesArray = forecastJson.getJSONArray(RESULTS);
        Movie[] parsedMoviesData = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJSON = moviesArray.getJSONObject(i);
            Movie movie = new Movie(movieJSON.getString(TITLE),
                    movieJSON.getString(POSTER_PATH),
                    movieJSON.getString(OVERVIEW),
                    movieJSON.getString(RELEASE_DATE).substring(0,4),
                    movieJSON.getString(VOTE_AVERAGE));

            parsedMoviesData[i] = movie;
        }

        return parsedMoviesData;
    }
}