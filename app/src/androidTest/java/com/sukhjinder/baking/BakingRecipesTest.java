package com.sukhjinder.baking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sukhjinder.baking.Activities.MainActivity;
import com.sukhjinder.baking.Model.Recipe;
import com.sukhjinder.baking.Widget.BakingPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class BakingRecipesTest {

    protected GlobalApplication globalApplication;
    protected IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        globalApplication = (GlobalApplication) activityTestRule.getActivity().getApplicationContext();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        idlingResource = globalApplication.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void checkRecipeRecyclerView() {
        onView(withId(R.id.recipes_recycler)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.recipes_recycler)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkAddToWidget() {
        onView(withId(R.id.recipes_recycler)).perform(RecyclerViewActions.scrollToPosition(2));

        onView(withId(R.id.recipes_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.add_recipe_to_widget))
                .check(matches(isDisplayed()))
                .perform(click());

        Recipe recipe = BakingPreferences.loadRecipe(globalApplication);
        assertNotNull(recipe);
    }
}