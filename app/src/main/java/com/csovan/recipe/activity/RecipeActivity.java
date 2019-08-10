package com.csovan.recipe.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.csovan.recipe.R;
import com.csovan.recipe.adapter.RecipeAdapter;
import com.csovan.recipe.model.Recipe;
import com.csovan.recipe.network.RecipeClient;
import com.csovan.recipe.network.RecipeService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.csovan.recipe.utils.Constant.RECIPE_LIST_STATE;

public class RecipeActivity extends AppCompatActivity {

    private final String TAG = RecipeActivity.class.getSimpleName();

    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList = new ArrayList<>();

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        if (savedInstanceState != null){
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);

            recipeAdapter = new RecipeAdapter(RecipeActivity.this, recipeList);

            RecyclerView.LayoutManager layoutManager;

            layoutManager = new GridLayoutManager(RecipeActivity.this,
                    getResources().getInteger(R.integer.num_col));

            rvRecipe.setLayoutManager(layoutManager);
            rvRecipe.setHasFixedSize(true);
            rvRecipe.setAdapter(recipeAdapter);

        }
        else {
            getRecipes();
        }
    }

    private void getRecipes() {

        RecipeService recipeService = RecipeClient.getRecipesFromUrl().create(RecipeService.class);
        Call<ArrayList<Recipe>> call = recipeService.getRecipesList();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {

                recipeList = response.body();

                if (recipeList != null) {
                    Log.d(TAG, "Recipe List: " + recipeList.size());
                    for (int i = 0; i < recipeList.size(); i++){
                        if (recipeList != null) {
                            Log.d(TAG, "Recipe Name: " + recipeList.get(i).getName());
                        }
                    }
                }

                recipeAdapter = new RecipeAdapter(RecipeActivity.this, recipeList);

                RecyclerView.LayoutManager layoutManager;
                layoutManager = new GridLayoutManager(RecipeActivity.this,
                        getResources().getInteger(R.integer.num_col));

                rvRecipe.setLayoutManager(layoutManager);
                rvRecipe.setHasFixedSize(true);
                rvRecipe.setAdapter(recipeAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }

    private void showErrorMessage() {
        Toast.makeText(RecipeActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, recipeList);
    }
}
