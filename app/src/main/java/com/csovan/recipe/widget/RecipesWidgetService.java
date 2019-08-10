package com.csovan.recipe.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesWidgetService extends IntentService {

    private static final String ACTION_DISPLAY_RECIPE = "com.csovan.recipe.action.display_recipe";
    private static final String EXTRA_RECIPE_NAME = "com.csovan.recipe.extra.recipe_name";
    private static final String EXTRA_INGREDIENTS = "com.csovan.recipe.extra.ingredients";

    public RecipesWidgetService() {
        super("RecipeWidgetService");
    }

    public static void startActionDisplayRecipe(Context context, String recipeName, String ingredients) {

        Intent intent = new Intent(context, RecipesWidgetService.class);
        intent.setAction(ACTION_DISPLAY_RECIPE);
        intent.putExtra(EXTRA_RECIPE_NAME, recipeName);
        intent.putExtra(EXTRA_INGREDIENTS, ingredients);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISPLAY_RECIPE.equals(action)) {
                final String recipeName = intent.getStringExtra(EXTRA_RECIPE_NAME);
                final String ingredients = intent.getStringExtra(EXTRA_INGREDIENTS);
                handleActionDisplayRecipe(recipeName, ingredients);
            }
        }
    }

    private void handleActionDisplayRecipe(String recipeName, String ingredients) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipesWidgetProvider.class));
        //Now update all widgets
        RecipesWidgetProvider.updateAppWidget(this, appWidgetManager, recipeName, ingredients, appWidgetIds);
    }
}

