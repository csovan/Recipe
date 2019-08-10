package com.csovan.recipe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.csovan.recipe.activity.RecipeActivity;
import com.csovan.recipe.activity.StepDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipesAppTests {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void recipeAppTests() {

        onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));

        //Recyclerview scroll to position
        onView(ViewMatchers.withId(R.id.rv_recipes)).perform(RecyclerViewActions.scrollToPosition(4));

    }

}
