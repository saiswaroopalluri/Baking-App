package com.example.android.bakingapp.network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.utils.NetworkUtility;

import java.net.URL;

public class RecipeAsyncTaskLoader extends AsyncTaskLoader<String> {

    private String jsonString;
    private URL url;

    public RecipeAsyncTaskLoader(Context context, URL url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        if (jsonString != null) {
            deliverResult(jsonString);
        } else {
            forceLoad();
        }
    }


    @Nullable
    @Override
    public String loadInBackground() {
        try {
            return NetworkUtility.getResponseFromHttpUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable String data) {
        jsonString = data;
        super.deliverResult(data);
    }
}