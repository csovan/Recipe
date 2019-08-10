package com.csovan.recipe.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.csovan.recipe.utils.Constant.RECIPE_BASE_URL;

public class RecipeClient {

    private static final String TAG = RecipeClient.class.getSimpleName();

    private static Retrofit retrofit = null;

    public static Retrofit getRecipesFromUrl() {

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(RECIPE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}