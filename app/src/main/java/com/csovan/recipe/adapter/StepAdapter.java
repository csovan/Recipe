package com.csovan.recipe.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csovan.recipe.R;
import com.csovan.recipe.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final ArrayList<Step> stepList;

    private final StepAdapterOnClickHandler stepClickHandler;

    public interface StepAdapterOnClickHandler{
        void onStepClick(int position);
    }

    public StepAdapter(ArrayList<Step> stepList, StepAdapterOnClickHandler stepClickHandler) {
        this.stepList = stepList;
        this.stepClickHandler = stepClickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_step, parent, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, int position) {

        if (stepList != null) {
            if (holder.stepTitle != null) {
                holder.stepTitle.setText(stepList.get(position).getShortDescription());
            }
            if (holder.stepNumber != null) {
                holder.stepNumber.setText(String.valueOf(position));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepClickHandler.onStepClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (stepList != null){
            return stepList.size();
        }
        else {
            return 0;
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_step_title)
        TextView stepTitle;

        @Nullable
        @BindView(R.id.tv_step_number)
        TextView stepNumber;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
