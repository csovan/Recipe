package com.csovan.recipe.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
import static com.csovan.recipe.utils.Constant.STEP_INDEX_STATE;
import static com.csovan.recipe.utils.Constant.STEP_INTENT_EXTRA;
import static com.csovan.recipe.utils.Constant.STEP_LIST_STATE;
import static com.csovan.recipe.utils.Constant.STEP_SINGLE;

public class StepDetailsActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler {

    private ArrayList<Step> stepList;
    private ArrayList<Recipe> recipeList;
    private int stepIndex;
    boolean isLandscape;

    @Nullable
    @BindView(R.id.rv_step)
    RecyclerView rvStep;

    @BindView(R.id.fl_exoplayer_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.btn_next_step)
    Button nextButton;

    @BindView(R.id.btn_previous_step)
    Button previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        isLandscape = findViewById(R.id.step_details_land_mode) != null;

        if (savedInstanceState != null){
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);
            stepList = savedInstanceState.getParcelableArrayList(STEP_LIST_STATE);
            stepIndex = savedInstanceState.getInt(STEP_INDEX_STATE,0);
        }

        Intent intent = getIntent();
        if (intent != null && savedInstanceState == null){
            if (intent.hasExtra(RECIPE_INTENT_EXTRA)){
                recipeList = intent.getParcelableArrayListExtra(RECIPE_INTENT_EXTRA);
            }
            if (intent.hasExtra(STEP_INTENT_EXTRA)){
                stepList = intent.getParcelableArrayListExtra(STEP_INTENT_EXTRA);
            }
            if (intent.hasExtra(STEP_INDEX_INTENT_EXTRA)){
                stepIndex = intent.getIntExtra(STEP_INDEX_INTENT_EXTRA, 0);
            }
            getVideoFromStep(stepList.get(stepIndex));
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(
                    String.format(
                            "%s - %s",
                            recipeList.get(0).getName(),
                            getString(R.string.step_details_activity_label)));
        }

        ButterKnife.bind(this);
        handleUiForDevice();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepIndex > 0) {
                    stepIndex = stepIndex - 1;
                    getNewVideo(stepList.get(stepIndex));
                } else {
                    previousButton.setClickable(false);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepIndex < stepList.size() - 1) {
                    stepIndex = stepIndex + 1;
                    getNewVideo(stepList.get(stepIndex));
                } else {
                    nextButton.setClickable(false);
                }
            }
        });
    }

    public void getVideoFromStep(Step step){
        ExoPlayerFragment videoPlayerFragment = new ExoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_exoplayer_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void getNewVideo(Step step){
        ExoPlayerFragment videoPlayerFragment = new ExoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_exoplayer_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStepClick(int position) {
        stepIndex = position;
        getNewVideo(stepList.get(stepIndex));
    }

    public void handleUiForDevice(){
        if (isLandscape){
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, recipeList);
        outState.putParcelableArrayList(STEP_LIST_STATE, stepList);
        outState.putInt(STEP_INDEX_STATE, stepIndex);
    }


}
