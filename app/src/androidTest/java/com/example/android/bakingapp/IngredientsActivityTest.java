package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.Helpers.RecipeHelper;
import com.example.android.bakingapp.fragments.IngredientsFragment;
import com.example.android.bakingapp.models.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;


@RunWith(AndroidJUnit4.class)
public class IngredientsActivityTest {

    private static final String INTENT_KEY = "recipe";

    @Rule
    public IntentsTestRule<IngredientsActivity> intentsTestRule = new IntentsTestRule<IngredientsActivity>(IngredientsActivity.class){

        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Recipe recipe = RecipeHelper.getRecipesList(targetContext).get(0);
            Intent intent = new Intent(targetContext, IngredientsActivity.class);
            intent.putExtra(INTENT_KEY, recipe);
            return intent;
        }
    };


    @Test
    public void testHasTextIngredient() {
        onView(withId(R.id.tv_ingredients)).check(matches(withText(containsString("Nutella"))));
    }


    @Test
    public void testHasStepIntroductionRecipe() {
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
    }


    @Test
    public void testRecyclerViewAction() {
        onView(ViewMatchers.withId(R.id.recycler_view_recipe_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }

    @Test
    public void testNumberOfRecipeStepsDisplayed() {
        IngredientsFragment ingredientsFragment = (IngredientsFragment) intentsTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(IngredientsFragment.class.getSimpleName());
        RecyclerView recyclerView = ingredientsFragment.getView().findViewById(R.id.recycler_view_recipe_steps);
        int adapterListCount = recyclerView.getAdapter().getItemCount();
        Recipe recipe = RecipeHelper.getRecipesList(intentsTestRule.getActivity().getApplicationContext()).get(0);
        int stepList = recipe.getmRecipeSteps().size();
        assertThat(adapterListCount, is(stepList));
    }
}
