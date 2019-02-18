package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private int mRecipeId;
    private String mRecipeName;
    private List<Ingredient> mIngredients;
    private List<RecipeStep> mRecipeSteps;
    private int mServings;
    private String mImage;

    public Recipe() {

    }

    private Recipe(Parcel in) {
        mRecipeId = in.readInt();
        mRecipeName = in.readString();
        mIngredients = new ArrayList<>();
        in.readList(mIngredients, Ingredient.class.getClassLoader());
        mRecipeSteps = new ArrayList<>();
        in.readList(mRecipeSteps, RecipeStep.class.getClassLoader());
        mServings = in.readInt();
        mImage = in.readString();
    }

    public Recipe(int recipeId, String recipeName, List<Ingredient> ingredients, List<RecipeStep> recipeSteps, int servings, String image) {
        mRecipeId = recipeId;
        mRecipeName = recipeName;
        mIngredients = ingredients;
        mRecipeSteps = recipeSteps;
        mServings = servings;
        mImage = image;
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getmRecipeId() {
        return mRecipeId;
    }

    public void setmRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    public String getmRecipeName() {
        return mRecipeName;
    }

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public List<Ingredient> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<RecipeStep> getmRecipeSteps() {
        return mRecipeSteps;
    }

    public void setmRecipeSteps(List<RecipeStep> mRecipeSteps) {
        this.mRecipeSteps = mRecipeSteps;
    }

    public int getmServings() {
        return mServings;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRecipeId);
        dest.writeString(mRecipeName);
        dest.writeList(mIngredients);
        dest.writeList(mRecipeSteps);
        dest.writeInt(mServings);
        dest.writeString(mImage);
    }

}
