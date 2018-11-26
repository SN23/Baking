package com.sukhjinder.baking.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sukhjinder.baking.API.RecipeAPI;
import com.sukhjinder.baking.Activities.MainActivity;
import com.sukhjinder.baking.Adapter.RecipeAdapter;
import com.sukhjinder.baking.GlobalApplication;
import com.sukhjinder.baking.Model.Recipe;
import com.sukhjinder.baking.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipes_recycler)
    RecyclerView recipeRecyclerView;

    private List<Recipe> recipes;
    private final static String RECIPE_NAME_LIST = "recipeNameList";
    private GlobalApplication globalApplication;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipes != null || recipes.size() > 0) {
            outState.putParcelableArrayList(RECIPE_NAME_LIST, (ArrayList<? extends Parcelable>) recipes);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_NAME_LIST)) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_NAME_LIST);
            recipeRecyclerView.setAdapter(new RecipeAdapter(recipes, (MainActivity) getActivity()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);
        recyclerSetup();

        globalApplication = (GlobalApplication) Objects.requireNonNull(getActivity()).getApplicationContext();
        globalApplication.setIdleState(false);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_NAME_LIST)) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_NAME_LIST);
            recipeRecyclerView.setAdapter(new RecipeAdapter(recipes, (MainActivity) getActivity()));
        } else {
            apiCall();
        }

        return view;
    }

    private void apiCall() {
        String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeAPI client = retrofit.create(RecipeAPI.class);
        Call<List<Recipe>> call = client.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                recipeRecyclerView.setAdapter(new RecipeAdapter(recipes, (MainActivity) getActivity()));
                if (globalApplication.getIdlingResource() != null) {
                    globalApplication.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private void recyclerSetup() {
        boolean twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPane) {
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        } else {
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        recipeRecyclerView.setHasFixedSize(true);
    }

}