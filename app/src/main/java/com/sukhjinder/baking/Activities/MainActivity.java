package com.sukhjinder.baking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sukhjinder.baking.Adapter.RecipeAdapter;
import com.sukhjinder.baking.Model.Recipe;
import com.sukhjinder.baking.R;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeClick(Recipe clickedRecipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("RecipeDetailActivity", clickedRecipe);
        startActivity(intent);
    }
}