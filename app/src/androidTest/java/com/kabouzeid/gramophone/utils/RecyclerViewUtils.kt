package com.kabouzeid.gramophone.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

fun getCountFromRecyclerView(@IdRes recyclerViewId: Int): Int {
    var count = 0
    val matcher = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {}
        override fun matchesSafely(item: View?): Boolean {
            count = (item as RecyclerView).adapter!!.itemCount
            return true
        }
    }
    onView(allOf(withId(recyclerViewId), isDisplayed()))
        .check(matches(matcher))
    return count
}

fun clickItemInRecyclerByName(
    @IdRes recyclerViewId: Int,
    name: String
) {
    onView(allOf(withId(recyclerViewId), isDisplayed())).perform(
        actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText(name)), click()
        )
    )
}

fun clickChildItemInRecyclerByName(
    @IdRes recyclerViewId: Int,
    @IdRes childItemId: Int,
    name: String
) {
    onView(Matchers.allOf(withId(recyclerViewId), isDisplayed())).perform(
        actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText(name)), clickOnViewChild(childItemId)
        )
    )
}

fun clickChildItemInRecyclerByPosition(
    @IdRes recyclerViewId: Int,
    @IdRes childItemId: Int,
    position: Int
) {
    onView(Matchers.allOf(withId(recyclerViewId), isDisplayed())).perform(
        actionOnItemAtPosition<RecyclerView.ViewHolder>(position, clickOnViewChild(childItemId))
    )
}

fun checkNameItemInRecycler(@IdRes recyclerViewId: Int, name: String) {
    onView(Matchers.allOf(withId(recyclerViewId), isDisplayed())).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText(name))
        )
    )
}