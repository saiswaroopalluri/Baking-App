package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    public static List<Recipe> getRecipeslistFromJSONString(String recipeJSONString) throws JSONException {

        /* Recipe Constants */
        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        /* Ingredients Constants */
        final String ING_QUANTITY = "quantity";
        final String ING_MEASURE = "measure";
        final String ING_INGREDIENT = "ingredient";

        /* Recipe Steps */
        final String RECIPE_STEP_ID = "id";
        final String RECIPE_STEP_SHORT_DESC = "shortDescription";
        final String RECIPE_STEP_DESCRIPTION = "description";
        final String RECIPE_STEP_VIDEO_URL = "videoURL";
        final String RECIPE_STEP_THUMB_NAIL_URL = "thumbnailURL";

        /* Movie array to hold each movies data */
        ArrayList<Recipe> parsedRecipesArray = new ArrayList<>();

        JSONArray resultsArray = new JSONArray(recipeJSONString);
        for (int i = 0; i < resultsArray.length(); i++) {
            int recipeId, servings;
            String recipeName, image;
            ArrayList<Ingredient> ingredients;
            ArrayList<RecipeStep> recipeSteps;

            JSONObject recipeJSONObject = resultsArray.getJSONObject(i);
            recipeId = recipeJSONObject.getInt(RECIPE_ID);
            recipeName = recipeJSONObject.getString(RECIPE_NAME);

            ingredients = new ArrayList<>();
            JSONArray ingredientsJSONArray = recipeJSONObject.getJSONArray(RECIPE_INGREDIENTS);
            for (int ing = 0; ing < ingredientsJSONArray.length(); ing++) {
                double quantity;
                String measure, ingredient;
                JSONObject ingredientJSONObject = ingredientsJSONArray.getJSONObject(ing);
                quantity = ingredientJSONObject.getDouble(ING_QUANTITY);
                measure = ingredientJSONObject.getString(ING_MEASURE);
                ingredient = ingredientJSONObject.getString(ING_INGREDIENT);

                Ingredient ingredientObj = new Ingredient(quantity, measure, ingredient);
                ingredients.add(ingredientObj);
            }

            recipeSteps = new ArrayList<>();
            JSONArray recipeStepsJSONArray = recipeJSONObject.getJSONArray(RECIPE_STEPS);
            for (int step = 0; step < recipeStepsJSONArray.length(); step++) {
                int stepId;
                String shortDescription, description, videoURL, thumbnailURL;

                JSONObject recipeStepJSONObject = recipeStepsJSONArray.getJSONObject(step);
                stepId = recipeStepJSONObject.getInt(RECIPE_STEP_ID);
                shortDescription = recipeStepJSONObject.getString(RECIPE_STEP_SHORT_DESC);
                description = recipeStepJSONObject.getString(RECIPE_STEP_DESCRIPTION);
                videoURL = recipeStepJSONObject.getString(RECIPE_STEP_VIDEO_URL);
                thumbnailURL = recipeStepJSONObject.getString(RECIPE_STEP_THUMB_NAIL_URL);

                RecipeStep recipeStep = new RecipeStep(stepId, shortDescription, description, videoURL, thumbnailURL);
                recipeSteps.add(recipeStep);
            }

            servings = recipeJSONObject.getInt(RECIPE_SERVINGS);
            image = recipeJSONObject.getString(RECIPE_IMAGE);

            Recipe recipe = new Recipe(recipeId, recipeName, ingredients, recipeSteps, servings, image);
            parsedRecipesArray.add(recipe);
        }

        return parsedRecipesArray;
    }

}
