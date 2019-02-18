package com.example.android.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Interfaces.RecipeStepItemClickListener;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {
    private List<RecipeStep> mRecipeSteps;
    private RecipeStepItemClickListener mRecipeStepItemClickListener;

    public RecipeStepAdapter(List<RecipeStep> recipeSteps, RecipeStepItemClickListener ingredientClickListener) {
        mRecipeSteps = recipeSteps;
        mRecipeStepItemClickListener = ingredientClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_short_description)
        TextView textViewShortDescription;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.recipe_step_list_item, parent, false);
        final ViewHolder viewHolderStep = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipeStepItemClickListener.onRecipeStepClick(viewHolderStep.getAdapterPosition());
            }
        });
        return viewHolderStep;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeStep recipeStep = mRecipeSteps.get(position);
        holder.textViewShortDescription.setText(recipeStep.getmShortDescription());
    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }

}
