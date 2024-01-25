package de.hdmstuttgart.trackmaster

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TrackMasterApplicationTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("de.hdmstuttgart.trackmaster", appContext.packageName)
    }

    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Test
    fun newActivityTest() {
        onView(withId(R.id.ic_home))
            .perform(click())

        onView(withId(R.id.floatingActionButton))
            .perform(click())

        onView(withId(R.id.fragment_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //todo: check if activity closes


        activityScenarioRule.scenario.close()
    }

    @Test
    fun mainActivityTest() {
        //test the bottom navigation bar
        onView(withId(R.id.ic_tracks))
            .perform(click())

        onView(withId(R.id.trackRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.ic_settings))
            .perform(click())

        onView(withId(R.id.darkLightSwitch))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.ic_home))
            .perform(click())

        onView(withId(R.id.statisticsFragmentView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.scoreFragmentView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //test the dark mode switch
        onView(withId(R.id.ic_settings))
            .perform(click())

        onView(withId(R.id.darkLightSwitch))
            .check(ViewAssertions.matches(ViewMatchers.isNotChecked()))

        onView(withId(R.id.darkLightSwitch))
            .perform(click())
            .check(ViewAssertions.matches(ViewMatchers.isChecked()))

        onView(withId(R.id.darkLightSwitch))
            .perform(click())
            .check(ViewAssertions.matches(ViewMatchers.isNotChecked()))

        activityScenarioRule.scenario.close()
    }

    @Test
    fun composeTest() {
        composeTestRule.onNodeWithTag("surface")
            .assertExists()
            .isDisplayed()


        composeTestRule.onNodeWithTag("dropdown")
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithText("Month")
            .assertExists()
            .performClick()

        val monthText = "Your statistics for " + LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        composeTestRule.onNodeWithTag("statisticsText")
            .assertExists()
            //.assert(hasText("Your statistics for January"))

        composeTestRule.onNodeWithTag("dropdown")
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithText("Year")
            .assertExists()
            .performClick()


        composeTestRule.onNodeWithTag("dropdown")
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithText("Week")
            .assertExists()
            .performClick()
    }
}