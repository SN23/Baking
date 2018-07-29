package com.sukhjinder.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sukhjinder.baking.Model.Ingredient;
import com.sukhjinder.baking.Model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetails extends AppCompatActivity {


    @BindView(R.id.ingredientsTV)
    TextView ingredientsTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("RecipeDetails");
        List<Ingredient> ingredients = recipe.getIngredients();

        String recipeName = recipe.getName();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipeName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        displayIngredientsList(ingredients);

    }


    private void displayIngredientsList(List<Ingredient> ingredientList) {
        StringBuilder builder = new StringBuilder();
        final String bullet = "\u25CF";
        for (Ingredient ingredient : ingredientList) {
            builder.append(bullet);
            builder.append(" ");
            builder.append(ingredient.getQuantity());
            builder.append(" ");
            builder.append(ingredient.getMeasure());
            builder.append(" ");
            builder.append(ingredient.getIngredient());
            builder.append('\n');
            builder.append('\n');
        }
        ingredientsTV.setText(builder.toString());
    }
}
