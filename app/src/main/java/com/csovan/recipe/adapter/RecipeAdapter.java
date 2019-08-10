package com.csovan.recipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csovan.recipe.activity.IngredientActivity;
import com.csovan.recipe.R;
import com.csovan.recipe.model.Recipe;
import com.csovan.recipe.utils.GlideApp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.csovan.recipe.utils.Constant.RECIPE_INTENT_EXTRA;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private Context context;
    private ArrayList<Recipe> recipeList;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe, parent,false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {

        if (recipeList != null){
            if (holder.recipeName != null) {
                holder.recipeName.setText(recipeList.get(position).getName());
            }
            if (holder.servingNum != null){
                holder.servingNum.setText(String.valueOf(recipeList.get(position).getServings()));
            }

            switch (position){
                case 0:
                    if (holder.recipePoster != null) {
                        GlideApp.with(context)
                             .load(R.drawable.nutellapie)
                             .into(holder.recipePoster);
                    }
                    break;
                case 1:
                    if (holder.recipePoster != null) {
                        GlideApp.with(context)
                             .load(R.drawable.brownies)
                             .into(holder.recipePoster);
                    }
                    break;
                case 2:
                    if (holder.recipePoster != null) {
                        GlideApp.with(context)
                              .load(R.drawable.yellowcake)
                              .into(holder.recipePoster);
                    }
                    break;
                case 3:
                    if (holder.recipePoster != null) {
                        GlideApp.with(context)
                             .load(R.drawable.cheesecake)
                             .into(holder.recipePoster);
                    }
                    break;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = recipeList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, IngredientActivity.class);
                ArrayList<Recipe> recipeList = new ArrayList<>();
                recipeList.add(recipe);
                intent.putParcelableArrayListExtra(RECIPE_INTENT_EXTRA, recipeList);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipeList != null){
            return recipeList.size();
        }else {
            return 0;
        }
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.iv_recipe_poster)
        ImageView recipePoster;

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        @Nullable
        @BindView(R.id.tv_serving_number)
        TextView servingNum;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
