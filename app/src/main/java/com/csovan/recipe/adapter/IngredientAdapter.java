package com.csovan.recipe.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csovan.recipe.R;
import com.csovan.recipe.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;

    public IngredientAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientViewHolder holder, int position) {

        if (ingredientList != null){
            if (holder.ingredientName != null){
                holder.ingredientName.setText(ingredientList.get(position).getIngredient());
            }
            if (holder.unitNumber != null) {
                holder.unitNumber.setText(String.valueOf(ingredientList.get(position).getQuantity()));
            }
            if (holder.unitMeasure != null) {
                holder.unitMeasure.setText(String.valueOf(ingredientList.get(position).getMeasure()));
            }
            if (holder.ingredientChecked != null) {
                holder.ingredientChecked.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ingredientChecked != null) {
                    if(holder.ingredientChecked.getVisibility() == View.GONE){
                        holder.ingredientChecked.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.ingredientChecked.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null){
            return ingredientList.size();
        }else {
            return 0;
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_ingredient_title)
        TextView ingredientName;

        @Nullable
        @BindView(R.id.tv_quantity_number)
        TextView unitNumber;

        @Nullable
        @BindView(R.id.tv_quantity_measure)
        TextView unitMeasure;

        @Nullable
        @BindView(R.id.iv_ingredient_checked)
        ImageView ingredientChecked;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
