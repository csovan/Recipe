package com.csovan.recipe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csovan.recipe.R;
import com.csovan.recipe.adapter.IngredientAdapter;
import com.csovan.recipe.model.Ingredient;
import com.csovan.recipe.model.Recipe;
import com.csovan.recipe.widget.RecipesWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.csovan.recipe.utils.Constant.RECIPE_INTENT_EXTRA;
import static com.csovan.recipe.utils.Constant.RECIPE_LIST_STATE;

public class IngredientActivity extends AppCompatActivity{

    private final String TAG = IngredientActivity.class.getSimpleName();

    private ArrayList<Recipe> recipeList;
    private List<Ingredient> ingredientList;
    String recipeName;

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView rvIngredient;

    @BindView(R.id.btn_view_steps)
    Button btnViewSteps;

    @Nullable
    @BindView(R.id.widget_ingredients_list)
    TextView tvWidgetIngredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ButterKnife.bind(this);


        if (savedInstanceState != null){
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);

            if (recipeList != null) {
                ingredientList = recipeList.get(0).getIngredients();
            }

            IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientList);

            RecyclerView.LayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(this);

            rvIngredient.setLayoutManager(layoutManager);
            rvIngredient.setHasFixedSize(true);
            rvIngredient.setAdapter(ingredientAdapter);
        }

        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null){
            if (intent.hasExtra(RECIPE_INTENT_EXTRA))
            {
                recipeList = getIntent().getParcelableArrayListExtra(RECIPE_INTENT_EXTRA);
                recipeName = recipeList.get(0).getName();
                Log.d(TAG, "Recipe List: " + recipeList.size());
                ingredientList = recipeList.get(0).getIngredients();
                Log.d(TAG, "Ingredient List: " + ingredientList.size());

                IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientList);

                RecyclerView.LayoutManager layoutManager;
                layoutManager = new LinearLayoutManager(this);

                rvIngredient.setLayoutManager(layoutManager);
                rvIngredient.setHasFixedSize(true);
                rvIngredient.setAdapter(ingredientAdapter);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(
                String.format(
                    "%s - %s",
                    recipeList.get(0).getName(),
                    getString(R.string.ingredient_activity_label)));
        }

        String widgetIngredientsList = ingredientsToString();
        if (tvWidgetIngredientsList != null) {
            tvWidgetIngredientsList.setText(widgetIngredientsList);
        }
        shareRecipeIngredients(widgetIngredientsList);

        btnViewSteps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            Intent intent = new Intent(IngredientActivity.this, StepActivity.class);
            intent.putParcelableArrayListExtra(RECIPE_INTENT_EXTRA, recipeList);
            startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, recipeList);
    }

    private String ingredientsToString() {
        StringBuilder builder = new StringBuilder();

        for (Ingredient ingredient : ingredientList) {
            builder.append(String.valueOf(ingredient.getQuantity())).append(" ");
            builder.append(String.valueOf(ingredient.getMeasure())).append("  ");
            builder.append(ingredient.getIngredient()).append("\n");

        }
        return builder.toString();
    }

    private void shareRecipeIngredients(String ingredients) {

        RecipesWidgetService.startActionDisplayRecipe(getApplication(),recipeName, ingredients);
    }
}
