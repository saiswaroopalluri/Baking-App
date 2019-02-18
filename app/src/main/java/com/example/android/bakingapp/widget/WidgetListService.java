package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.google.gson.Gson;

import java.util.List;

public class WidgetListService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListFactory(this.getApplicationContext());
    }
}

class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> ingredientList;
    private Recipe recipeSelected;

    private static final String PREFERENCE_RECIPE_KEY = "recipe_widget";


    public WidgetListFactory(Context context) {
        mContext = context;

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String recipeJson = sharedPreferences.getString(PREFERENCE_RECIPE_KEY, null);
        recipeSelected = null;
        if (recipeJson != null) {
            Gson gson = new Gson();
            recipeSelected = gson.fromJson(recipeJson, Recipe.class);
        }

        if (recipeSelected != null) {
            ingredientList = recipeSelected.getmIngredients();
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (recipeSelected != null) {
            ingredientList = recipeSelected.getmIngredients();
        } else {
            ingredientList = null;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredientList == null) {
            return 0;
        }
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredientList == null || ingredientList.size() == 0) {
            return null;
        }
        Ingredient ingredient = ingredientList.get(position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.tv_widget_ingredient_name, ingredient.getmIngredient());
        remoteViews.setTextViewText(R.id.tv_widget_ingredient_quantity, String.valueOf(ingredient.getmQuantity()));
        remoteViews.setTextViewText(R.id.tv_widget_ingredient_measure, String.valueOf(ingredient.getmMeasure()));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
