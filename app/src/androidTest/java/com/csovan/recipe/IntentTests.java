package com.csovan.recipe;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.csovan.recipe.activity.IngredientActivity;
import com.csovan.recipe.activity.RecipeActivity;
import com.csovan.recipe.activity.StepActivity;
import com.csovan.recipe.activity.StepDetailsActivity;
import com.csovan.recipe.utils.Constant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.csovan.recipe.utils.Constant.RECIPE_INTENT_EXTRA;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class IntentTests {

    @Rule
    public IntentsTestRule<RecipeActivity> recipeActivityRule = new IntentsTestRule<>(RecipeActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void intentTests(){

        // Let the UI load completely first
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Perform Recyclerview click on item at position
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check if intent (RecipeActivity to RecipeDetailsActivity) has RECIPE_INTENT_EXTRA
        intended(hasExtraWithKey(RECIPE_INTENT_EXTRA));

        //Perform click action on view steps button
        onView(withId(R.id.btn_view_steps)).perform(ViewActions.click());

        //Check if intent IngredientActivity to StepActivity has RECIPE_INTENT_EXTRA
        intended(hasComponent(StepActivity.class.getName()));
    }
}
