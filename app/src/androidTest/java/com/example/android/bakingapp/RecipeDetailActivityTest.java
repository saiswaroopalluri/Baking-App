package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.Helpers.RecipeHelper;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.RecipeStep;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private static final String INTENT_KEY_RECIPE_STEPS = "recipe_steps";
    private static final String INTENT_KEY_RECIPE_STEP_POSITION = "recipe_step_position";
    private static final String INTENT_BUNDLE_RECIPE_EXTRA = "extra";

    private static final int STEP_POSITION = 0;

    @Rule
    public IntentsTestRule<RecipeDetailActivity> intentsTestRule = new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            Recipe recipe = RecipeHelper.getRecipesList(targetContext).get(0);
            Intent intent = new Intent(targetContext, RecipeDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(INTENT_KEY_RECIPE_STEPS, (ArrayList<RecipeStep>)recipe.getmRecipeSteps());
            bundle.putInt(INTENT_KEY_RECIPE_STEP_POSITION, STEP_POSITION);
            intent.putExtra(INTENT_BUNDLE_RECIPE_EXTRA, bundle);

            return intent;
        }
    };


    @Test
    public void testVideoDisplay() {
        onView(withId(R.id.exo_player_image_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testDescriptionDisplayed() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RecipeStep recipeStep = RecipeHelper.getRecipesList(targetContext).get(0).getmRecipeSteps().get(STEP_POSITION);
        onView(withText(recipeStep.getmDescription())).check(matches(isDisplayed()));
    }



}
