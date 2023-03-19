package ru.kkuzmichev.simpleappforespresso;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityIdlingTests {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void registerIdlingResources() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @After
    public void unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    @Test
    public void openGalleryListTest() {
        ViewInteraction menuButton = onView(childAtPosition(allOf(withId(R.id.toolbar),
            childAtPosition(
                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
               0)),
           1));
        menuButton.check(matches(isDisplayed()));
        menuButton.perform(click());

        ViewInteraction galleryButton = onView(withId(R.id.nav_gallery));
        galleryButton.check(matches(isDisplayed()));
        galleryButton.perform(click());

        ViewInteraction numberView = onView(allOf(withId(R.id.item_number), withText("7")));
        numberView.check(matches(isDisplayed()));
        numberView.check(matches(withText("7")));
    }

    @Test
    public void galleryItemsListTest() {
        ViewInteraction menuButton = onView(childAtPosition(allOf(withId(R.id.toolbar),
                        childAtPosition(
                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                0)),
                1));
        menuButton.check(matches(isDisplayed()));
        menuButton.perform(click());

        ViewInteraction galleryButton = onView(withId(R.id.nav_gallery));
        galleryButton.check(matches(isDisplayed()));
        galleryButton.perform(click());

        ViewInteraction recycleView = onView(withId(R.id.recycle_view));
        recycleView.check(matches(isDisplayed()));
        recycleView.check(matches(CustomViewMatcher.recyclerViewSizeMatcher(10)));
        recycleView.check(CustomViewAssertions.isRecyclerView());

    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}