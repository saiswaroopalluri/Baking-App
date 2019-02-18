package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.bakingapp.Adapters.RecipeAdapter;
import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.IngredientsActivity;
import com.example.android.bakingapp.Interfaces.RecipeItemClickListener;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.network.RecipeLoaderCallbacks;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtility;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeFragment extends Fragment implements RecipeLoaderCallbacks.RecipesLoaderListener {

    private static final String INTENT_KEY = "recipe";
    private static final int ID_RECIPE_FRAGMENT_LOADER = 123;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    private Context mContext;
    private List<Recipe> recipeList;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    public RecipeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context context = getContext();

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);

        recipeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recipeRecyclerView.setLayoutManager(linearLayoutManager);

        getIdlingResource();
        getRecipes();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getRecipes() {
        URL recipesURL = NetworkUtility.buildRecipesUrl();
        RecipeLoaderCallbacks recipeLoaderCallbacks = new RecipeLoaderCallbacks(getContext(), recipesURL, this);
        try {
            getActivity().getSupportLoaderManager().initLoader(ID_RECIPE_FRAGMENT_LOADER, null, recipeLoaderCallbacks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecipes() {
        if (recipeList == null) {
            return;
        }

        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipeList, new RecipeItemClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                Intent intent = new Intent(getContext(), IngredientsActivity.class);
                intent.putExtra(INTENT_KEY, recipe);
                startActivity(intent);
            }
        });

        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    // region RecipesLoaderListener methods
    @Override
    public void onPreExecute() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(String jsonString) {
        mProgressBar.setVisibility(View.INVISIBLE);
        try {
            getActivity().getSupportLoaderManager().destroyLoader(ID_RECIPE_FRAGMENT_LOADER);
            recipeList = JsonUtils.getRecipeslistFromJSONString(jsonString);
            loadRecipes();
            mIdlingResource.setIdleState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // endregion

}
