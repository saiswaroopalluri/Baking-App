package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.fragments.RecipeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeFragment recipeFragment = (RecipeFragment) fragmentManager.findFragmentByTag(RecipeFragment.class.getSimpleName());
        if (recipeFragment == null) {
            recipeFragment = new RecipeFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_container, recipeFragment, RecipeFragment.class.getSimpleName())
                    .commit();
        }
    }
}
