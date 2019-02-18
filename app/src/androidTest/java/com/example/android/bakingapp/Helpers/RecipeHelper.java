package com.example.android.bakingapp.Helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.RecipeStep;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecipeHelper {
    public static List<Recipe> getRecipesList(Context context) {
        JsonReader reader;
        ArrayList<Recipe> recipeList = new ArrayList<>();
        try {
            reader = readJSONFile(context);
            reader.beginArray();
            while (reader.hasNext()) {
                recipeList.add(readRecipeEntry(reader));
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipeList;
    }

    /* Helper Methods */
    private static Recipe readRecipeEntry(JsonReader reader) {
        int recipeId = -1;
        String recipeName = null;
        List<Ingredient> ingredients = new ArrayList<>();
        List<RecipeStep> recipeSteps = new ArrayList<>();
        int servings = -1;
        String image = null;
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        recipeId = reader.nextInt();
                        break;
                    case "name":
                        recipeName = reader.nextString();
                        break;
                    case "ingredients":
                        ingredients = getIngredientsList(reader);
                        break;
                    case "steps":
                        recipeSteps = getParsedRecipeStepList(reader);
                        break;
                    case "servings":
                        servings = reader.nextInt();
                        break;
                    case  "image":
                        image = reader.nextString();
                        break;
                }
            }
            reader.endObject();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new Recipe(recipeId, recipeName, ingredients, recipeSteps, servings, image);
    }

    /* Parsing Ingredients */
    private static List<Ingredient> getIngredientsList(JsonReader reader) {
        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                ingredientsList.add(readIngredientEntry(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ingredientsList;
    }

    private static Ingredient readIngredientEntry(JsonReader reader) {
        double quantity = -1;
        String measure = null;
        String ingredient = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "quantity":
                        quantity = reader.nextDouble();
                        break;
                    case "measure":
                        measure = reader.nextString();
                        break;
                    case "ingredient":
                        ingredient = reader.nextString();
                        break;
                }
            }
            reader.endObject();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new Ingredient(quantity, measure, ingredient);
    }
    /* End of Parsing Ingredients */

    /* Recipe Steps Parsing */
    private static List<RecipeStep> getParsedRecipeStepList(JsonReader reader) {
        ArrayList<RecipeStep> recipeStepsList = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                recipeStepsList.add(readRecipeStepEntry(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipeStepsList;
    }

    private static RecipeStep readRecipeStepEntry(JsonReader reader) {
        int stepId = -1;
        String shortDescription = null;
        String description = null;
        String videoURL = null;
        String thumbnailURL = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        stepId = reader.nextInt();
                        break;
                    case "shortDescription":
                        shortDescription = reader.nextString();
                        break;
                    case "description":
                        description = reader.nextString();
                        break;
                    case "videoURL":
                        videoURL = reader.nextString();
                        break;
                    case "thumbnailURL":
                        thumbnailURL = reader.nextString();
                        break;
                }
            }
            reader.endObject();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new RecipeStep(stepId, shortDescription, description, videoURL, thumbnailURL);
    }
    /* End of Recipe Steps Parsing */

    private static JsonReader readJSONFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        String uri = null;

        try {
            for (String asset : assetManager.list("")) {
                if (asset.endsWith("baking.json")) {
                    uri = "asset:///" + asset;
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, R.string.sample_list_load_error, Toast.LENGTH_LONG)
                    .show();
        }

        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        DataSource dataSource = new DefaultDataSource(context, null, userAgent, false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } finally {
            Util.closeQuietly(dataSource);
        }

        return reader;

    }

}
