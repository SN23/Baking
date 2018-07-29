package com.sukhjinder.baking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sukhjinder.baking.Model.Recipe;
import com.sukhjinder.baking.R;
import com.sukhjinder.baking.RecipeDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private Context context;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, final int position) {
        holder.bind(recipes.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = recipes.get(position);

                Intent intent = new Intent(context, RecipeDetails.class);
                intent.putExtra("RecipeDetails", recipe);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_servings)
        TextView recipeServings;
        @BindView(R.id.recipe_steps)
        TextView recipeSteps;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Recipe recipe) {
            recipeName.setText(recipe.getName());
            recipeServings.setText(Integer.toString(recipe.getServings()));
            recipeSteps.setText(Integer.toString(recipe.getSteps().size()));
        }
    }
}