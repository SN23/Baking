package com.sukhjinder.baking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sukhjinder.baking.Adapter.RecipeStepNameAdapter;
import com.sukhjinder.baking.Model.Ingredient;
import com.sukhjinder.baking.Model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailsFragment extends Fragment {

    @BindView(R.id.recipe_steps_recycler_view)
    RecyclerView recipeStepsRecyclerView;

    @BindView(R.id.ingredientsTV)
    TextView ingredientsTV;

    private List<Step> steps;
    private List<Ingredient> ingredients;

    public RecipeDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            steps = args.getParcelableArrayList("stepsList");
            ingredients = args.getParcelableArrayList("ingredientsList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

        displayIngredientsList(ingredients);
        displaySteps(steps);

        return view;
    }

    private void displaySteps(List<Step> steps) {
        recipeStepsRecyclerView.setFocusable(false);
        recipeStepsRecyclerView.setHasFixedSize(true);
        recipeStepsRecyclerView.setNestedScrollingEnabled(false);
        recipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeStepsRecyclerView.setAdapter(new RecipeStepNameAdapter(steps, (RecipeDetailActivity) getActivity()));
    }

    private void displayIngredientsList(List<Ingredient> ingredientList) {
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredientList) {
            builder.append("\u2022" + " ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append('\n')
                    .append('\n');
        }
        ingredientsTV.setText(builder.toString());
    }
}