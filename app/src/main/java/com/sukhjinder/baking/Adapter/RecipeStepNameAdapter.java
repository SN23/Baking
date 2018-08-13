package com.sukhjinder.baking.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sukhjinder.baking.Model.Step;
import com.sukhjinder.baking.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepNameAdapter extends RecyclerView.Adapter<RecipeStepNameAdapter.ViewHolder> {

    private List<Step> steps;
    private RecipeStepItemClickListener recipeStepItemClickListener;

    public RecipeStepNameAdapter(List<Step> steps, RecipeStepItemClickListener recipeStepItemClickListener) {
        this.steps = steps;
        this.recipeStepItemClickListener = recipeStepItemClickListener;
    }

    @NonNull
    @Override
    public RecipeStepNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepNameAdapter.ViewHolder holder, int position) {
        holder.bind(steps.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recipeStepItemClickListener.onStepClick(steps.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_step_title)
        TextView recipeStepTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Step step) {
            recipeStepTitle.setText(step.getShortDescription());
        }
    }

    public interface RecipeStepItemClickListener {
        void onStepClick(Step step, int position);
    }
}