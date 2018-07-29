package com.sukhjinder.baking.API;

import com.sukhjinder.baking.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
