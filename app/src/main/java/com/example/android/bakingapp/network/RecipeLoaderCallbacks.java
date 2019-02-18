package com.example.android.bakingapp.network;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.net.URL;

public class RecipeLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {

    private Context context;
    private URL url;
    private RecipesLoaderListener recipesLoaderListener;

    public interface RecipesLoaderListener {
        void onPreExecute();
        void onPostExecute(String jsonString);
    }

    public RecipeLoaderCallbacks(Context context, URL url, RecipesLoaderListener recipesLoaderListener) {
        this.context = context;
        this.url = url;
        this.recipesLoaderListener = recipesLoaderListener;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        recipesLoaderListener.onPreExecute();
        return new RecipeAsyncTaskLoader(context, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        recipesLoaderListener.onPostExecute(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
