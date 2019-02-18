package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.fragments.RecipeDetailFragment;
import com.example.android.bakingapp.models.RecipeStep;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnDetailFragmentInteractionListener {

    private static final String INTENT_KEY_RECIPE_STEPS = "recipe_steps";
    private static final String INTENT_KEY_RECIPE_STEP_POSITION = "recipe_step_position";
    private static final String INTENT_BUNDLE_RECIPE_EXTRA = "extra";


    private List<RecipeStep> recipeStepList;
    private RecipeDetailFragment recipeDetailFragment;
    private int stepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intentCalled = getIntent();
        if (intentCalled != null) {
            if (intentCalled.getBundleExtra(INTENT_BUNDLE_RECIPE_EXTRA) != null) {
                Bundle bundle = intentCalled.getBundleExtra(INTENT_BUNDLE_RECIPE_EXTRA);
                try {
                    recipeStepList = bundle.getParcelableArrayList(INTENT_KEY_RECIPE_STEPS);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                stepPosition = bundle.getInt(INTENT_KEY_RECIPE_STEP_POSITION);
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        recipeDetailFragment = (RecipeDetailFragment) fragmentManager.findFragmentByTag(RecipeDetailFragment.class.getSimpleName());
        if (recipeDetailFragment == null) {
            recipeDetailFragment = new RecipeDetailFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_recipe_container, recipeDetailFragment, RecipeDetailFragment.class.getSimpleName())
                    .commit();
        }

        recipeDetailFragment.setRecipeStepList(recipeStepList, false);

    }

    @Override
    public void onDetailFragmentInteraction() {
        recipeDetailFragment.refreshToPosition(stepPosition);
    }
}
