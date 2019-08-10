package com.csovan.recipe.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.csovan.recipe.R;
import com.csovan.recipe.adapter.StepAdapter;
import com.csovan.recipe.model.Recipe;
import com.csovan.recipe.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.csovan.recipe.utils.Constant.RECIPE_INTENT_EXTRA;
import static com.csovan.recipe.utils.Constant.RECIPE_LIST_STATE;
import static com.csovan.recipe.utils.Constant.STEP_INDEX_INTENT_EXTRA;
import static com.csovan.recipe.utils.Constant.STEP_INTENT_EXTRA;

public class StepActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler {

    private final String TAG = StepActivity.class.getSimpleName();

    private ArrayList<Recipe> recipeList;
    private ArrayList<Step> stepList;

    @Nullable
    @BindView(R.id.rv_step)
    RecyclerView rvStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if (savedInstanceState != null){
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);

            if (recipeList != null) {
                stepList = (ArrayList<Step>) recipeList.get(0).getSteps();
            }

            StepAdapter stepAdapter = new StepAdapter(stepList, this);

            RecyclerView.LayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(this);
            if (rvStep != null) {
                rvStep.setLayoutManager(layoutManager);
            }

            if (rvStep != null) {
                rvStep.setAdapter(stepAdapter);
            }
        }

        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null){
            if (intent.hasExtra(RECIPE_INTENT_EXTRA)){
                recipeList = intent.getParcelableArrayListExtra(RECIPE_INTENT_EXTRA);
                Log.d(TAG, "Recipe List: " + recipeList.size());
                stepList = (ArrayList<Step>) recipeList.get(0).getSteps();
                Log.d(TAG, "Step List: " + stepList.size());

                StepAdapter stepAdapter = new StepAdapter(stepList, this);

                RecyclerView.LayoutManager layoutManager;
                layoutManager = new LinearLayoutManager(this);

                if (rvStep != null) {
                    rvStep.setLayoutManager(layoutManager);
                }
                if (rvStep != null) {
                    rvStep.setAdapter(stepAdapter);
                }
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(
                    String.format(
                            "%s - %s",
                            recipeList.get(0).getName(),
                            getString(R.string.step_activity_label)));
        }
    }

    @Override
    public void onStepClick(int position) {
        Intent intent = new Intent(StepActivity.this, StepDetailsActivity.class);
        intent.putParcelableArrayListExtra(STEP_INTENT_EXTRA, stepList);
        intent.putParcelableArrayListExtra(RECIPE_INTENT_EXTRA, recipeList);
        intent.putExtra(STEP_INDEX_INTENT_EXTRA, position);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, recipeList);
    }
}
