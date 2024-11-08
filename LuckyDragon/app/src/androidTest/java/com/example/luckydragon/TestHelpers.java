package com.example.luckydragon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.espresso.NoMatchingViewException;

public class TestHelpers {
    // source: https://stackoverflow.com/questions/22358325/how-to-wait-till-a-view-has-gone-in-espresso-tests
    public static void waitForViewToAppear(int viewId, long maxWaitingTimeMs) {
        long endTime = System.currentTimeMillis() + maxWaitingTimeMs;
        while (System.currentTimeMillis() <= endTime) {
            try {
                onView(allOf(withId(viewId), isDisplayed())).check(matches(not(doesNotExist())));
                return;
            } catch (NoMatchingViewException ignored) {
            }
        }
        throw new RuntimeException("timeout exceeded"); // or whatever exception you want
    }
    public static void waitForViewToBeEnabled(int viewId, long maxWaitingTimeMs) {
        long endTime = System.currentTimeMillis() + maxWaitingTimeMs;
        while (System.currentTimeMillis() <= endTime) {
            try {
                onView(allOf(withId(viewId), isEnabled())).check(matches(not(doesNotExist())));
                return;
            } catch (NoMatchingViewException ignored) {
            }
        }
        throw new RuntimeException("timeout exceeded"); // or whatever exception you want
    }
}
