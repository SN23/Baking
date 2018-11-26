package com.sukhjinder.baking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sukhjinder.baking.Adapter.RecipeStepNameAdapter;
import com.sukhjinder.baking.Model.Ingredient;
import com.sukhjinder.baking.Model.Recipe;
import com.sukhjinder.baking.Model.Step;
import com.sukhjinder.baking.R;
import com.sukhjinder.baking.Fragments.RecipeDetailsFragment;
import com.sukhjinder.baking.Fragments.RecipeStepFragment;
import com.sukhjinder.baking.Widget.BakingWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepNameAdapter.RecipeStepItemClickListener {

    private boolean twoPane;
    private int totalNumberOfSteps;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private int pos;
    private Recipe recipe;
    private String recipeName;

    private RecipeStepFragment recipeStepFragment;
    private RecipeDetailsFragment recipeDetailsFragment;
    private FragmentManager fragmentManager;
    private ActionBar actionBar;

    private static String STACK_RECIPE_STEPS_FRAGMENT = "StackRecipeStepsFragment";
    private static String RECIPE_STEP_POSITION = "recipeStepPosition";
    private static String RECIPE_STEPS = "recipeSteps";
    private static String RECIPE_INGREDIENTS = "recipeIngredients";
    private static String RECIPE_NAME = "recipeName";
    private static String BUTTON_VISIBILITY = "buttonVisibility";

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
        twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        actionBar = getSupportActionBar();

        if (!twoPane) {
            ButterKnife.bind(this);
        }

        if (savedInstanceState != null) {

            pos = savedInstanceState.getInt(RECIPE_STEP_POSITION);
            steps = savedInstanceState.getParcelableArrayList(RECIPE_STEPS);
            ingredients = savedInstanceState.getParcelableArrayList(RECIPE_INGREDIENTS);
            totalNumberOfSteps = steps.size() - 1;
            recipeName = savedInstanceState.getString(RECIPE_NAME);

            if (recipeName != null && actionBar != null) {
                actionBar.setTitle(recipeName);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            if (savedInstanceState.containsKey(BUTTON_VISIBILITY) && !twoPane) {
                buttonLayout.setVisibility(savedInstanceState.getInt(BUTTON_VISIBILITY));
                buttonSetup(pos);
                String stepNumber = pos + "/" + totalNumberOfSteps;
                stepNumberTV.setText(stepNumber);
            }
        }

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            recipe = intent.getParcelableExtra("RecipeDetailActivity");
            ingredients = recipe.getIngredients();
            steps = recipe.getSteps();
            totalNumberOfSteps = steps.size() - 1;

            recipeName = recipe.getName();

            if (actionBar != null) {
                actionBar.setTitle(recipeName);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            recipeStepFragment = new RecipeStepFragment();
            recipeDetailsFragment = new RecipeDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("stepsList", (ArrayList<? extends Parcelable>) steps);
            bundle.putParcelableArrayList("ingredientsList", (ArrayList<? extends Parcelable>) ingredients);
            recipeDetailsFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_details_container, recipeDetailsFragment)
                    .commit();

            twoPane = getResources().getBoolean(R.bool.twoPaneMode);
            if (twoPane) {
                Bundle stepBundle = new Bundle();
                stepBundle.putParcelable("step", steps.get(0));
                recipeStepFragment.setArguments(stepBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_step_container, recipeStepFragment)
                        .commit();

            }

        }
    }

    public void buttonSetup(int position) {

        pos = position;
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos < steps.size() - 1) {
                    pos = pos + 1;
                    String stepNumber = pos + "/" + totalNumberOfSteps;
                    stepNumberTV.setText(stepNumber);
                    Bundle bundle = new Bundle();
                    recipeStepFragment = new RecipeStepFragment();
                    bundle.putParcelable("step", steps.get(pos));
                    recipeStepFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_details_container, recipeStepFragment)
                            .addToBackStack(STACK_RECIPE_STEPS_FRAGMENT)
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
                    pos = pos - 1;
                    String stepNumber = pos + "/" + totalNumberOfSteps;
                    stepNumberTV.setText(stepNumber);
                    Bundle bundle = new Bundle();
                    recipeStepFragment = new RecipeStepFragment();
                    bundle.putParcelable("step", steps.get(pos));
                    recipeStepFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_details_container, recipeStepFragment)
                            .addToBackStack(STACK_RECIPE_STEPS_FRAGMENT)
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "First Step", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_recipe_to_widget) {
            BakingWidgetService.updateWidget(this, recipe);
            Toast.makeText(getApplicationContext(), "Added Recipe to Widget", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(RECIPE_STEP_POSITION, pos);
        outState.putParcelableArrayList(RECIPE_STEPS, (ArrayList<? extends Parcelable>) steps);
        outState.putParcelableArrayList(RECIPE_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
        outState.putString(RECIPE_NAME, recipeName);
        if (!twoPane) {
            outState.putInt(BUTTON_VISIBILITY, buttonLayout.getVisibility());
        }
    }

    //  https://stackoverflow.com/questions/20340303/in-fragment-on-back-button-pressed-activity-is-blank
    @Override
    public void onBackPressed() {
        int fragmentCount = fragmentManager.getBackStackEntryCount();

        if (fragmentCount == 0) {
            finish();
        }


        if (twoPane) {
            finish();
        }

        if (!twoPane) {
            fragmentManager.popBackStack(STACK_RECIPE_STEPS_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            buttonLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStepClick(Step step, final int position) {

        recipeStepFragment = new RecipeStepFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        recipeStepFragment.setArguments(bundle);

        if (twoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepFragment)
                    .addToBackStack(STACK_RECIPE_STEPS_FRAGMENT)
                    .commit();

        } else {
            buttonLayout.setVisibility(View.VISIBLE);
            buttonSetup(position);
            String stepNumber = position + "/" + totalNumberOfSteps;
            pos = position;
            stepNumberTV.setText(stepNumber);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_details_container, recipeStepFragment)
                    .addToBackStack(STACK_RECIPE_STEPS_FRAGMENT)
                    .commit();
        }
    }
}