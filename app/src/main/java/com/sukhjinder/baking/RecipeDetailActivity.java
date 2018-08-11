package com.sukhjinder.baking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sukhjinder.baking.Adapter.RecipeStepNameAdapter;
import com.sukhjinder.baking.Model.Ingredient;
import com.sukhjinder.baking.Model.Recipe;
import com.sukhjinder.baking.Model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepNameAdapter.RecipeStepItemClickListener {

    private FragmentManager fragmentManager;
    private RecipeDetailsFragment recipeDetailsFragment;
    private RecipeStepFragment recipeStepFragment;
    private boolean twoPane;
    private int totalNumberOfSteps;
    private List<Step> steps;
    private int pos;

    @BindView(R.id.next_step_button)
    Button nextStepButton;

    @BindView(R.id.previous_step_button)
    Button previousStepButton;

    @BindView(R.id.step_number)
    TextView stepNumberTV;

    @BindView(R.id.buttonLayout)
    RelativeLayout buttonLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("RecipeDetailActivity");
        List<Ingredient> ingredients = recipe.getIngredients();
        steps = recipe.getSteps();
        totalNumberOfSteps = steps.size() - 1;

        String recipeName = recipe.getName();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipeName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPane) {

            recipeStepFragment = new RecipeStepFragment();
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable("step", steps.get(0));
            recipeStepFragment.setArguments(stepBundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepFragment)
                    .commit();

        } else {
            ButterKnife.bind(this);
            buttonLayout.setVisibility(View.GONE);

            nextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pos < steps.size() - 1) {
                        pos++;
                        stepNumberTV.setText(pos + "/" + totalNumberOfSteps);
                        Bundle bundle = new Bundle();
                        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                        bundle.putParcelable("step", steps.get(pos));
                        recipeStepFragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .replace(R.id.recipe_details_container, recipeStepFragment)
                                .addToBackStack("RecipeDetailsFragment")
                                .commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Last Step", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            previousStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pos > 0) {
                        pos--;
                        stepNumberTV.setText((pos) + "/" + totalNumberOfSteps);
                        Bundle bundle = new Bundle();
                        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                        bundle.putParcelable("step", steps.get(pos));
                        recipeStepFragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .replace(R.id.recipe_details_container, recipeStepFragment)
                                .addToBackStack("RecipeDetailsFragment")
                                .commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "First Step", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }

        recipeDetailsFragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("stepsList", (ArrayList<? extends Parcelable>) steps);
        bundle.putParcelableArrayList("ingredientsList", (ArrayList<? extends Parcelable>) ingredients);
        recipeDetailsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.recipe_details_container, recipeDetailsFragment)
                .commit();


    }

    @Override
    public void onStepClick(Step step, final int position) {

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        pos = position;

        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        recipeStepFragment.setArguments(bundle);

        if (twoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepFragment)
                    .addToBackStack("RecipeStepFragment")
                    .commit();

        } else {
            buttonLayout.setVisibility(View.VISIBLE);
            stepNumberTV.setText(position + "/" + totalNumberOfSteps);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_details_container, recipeStepFragment)
                    .addToBackStack("RecipeDetailsFragment")
                    .commit();
        }
    }
}