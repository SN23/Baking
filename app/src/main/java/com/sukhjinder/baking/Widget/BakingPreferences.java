package com.sukhjinder.baking.Widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.sukhjinder.baking.Model.Recipe;

public class BakingPreferences {
    //    https://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
    public static final String PREFERENCE_NAME = "baking_prefs";

    public static void saveRecipe(Context context, Recipe recipe) {
        Editor prefs = context.getSharedPreferences(PREFERENCE_NAME, context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String jsonObj = gson.toJson(recipe);
        prefs.putString(PREFERENCE_NAME, jsonObj);
        prefs.commit();
    }

    public static Recipe loadRecipe(Context context) {

        Gson gson = new Gson();
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, context.MODE_PRIVATE);
        String recipeJson = prefs.getString(PREFERENCE_NAME, "");
        Recipe recipe = gson.fromJson(recipeJson, Recipe.class);

        return recipe;
    }
}