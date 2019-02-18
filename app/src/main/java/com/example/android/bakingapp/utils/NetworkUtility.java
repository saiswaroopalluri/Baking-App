package com.example.android.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtility {

    private static final String TAG = NetworkUtility.class.getSimpleName();

    private static final String RECIPE_SCHEME = "https";
    private static final String RECIPE_BASE_URL = "d17h27t6h515a5.cloudfront.net";
    private static final String RECIPE_APPENDED_PATH = "topher/2017/May/59121517_baking/baking.json";

    public static URL buildRecipesUrl() {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme(RECIPE_SCHEME)
                .authority(RECIPE_BASE_URL)
                .appendEncodedPath(RECIPE_APPENDED_PATH)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d(TAG, "Built URI " + url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Log.v("input stream", in.toString());

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



}
