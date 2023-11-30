package de.hdmstuttgart.trackmaster

import android.widget.Button
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class Assignment1Test {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun assignment1Test() {
        val appCompatButton = onView(allOf(instanceOf(Button::class.java), withId(R.id.button)))
            .check(matches(isDisplayed()))
        appCompatButton.perform(click())
        appCompatButton.perform(click())
        appCompatButton.perform(click())
        appCompatButton.perform(click())
        val textview = onView(allOf(instanceOf(TextView::class.java), withText("Clicked 4 times")))
            .check(matches(isDisplayed()))
        appCompatButton.perform(click())
        val textview2 = onView(allOf(instanceOf(TextView::class.java), withText("clicks: 5")))
            .check(matches(isDisplayed()))
    }
}