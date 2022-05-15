package com.kabouzeid.gramophone.utils

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.kabouzeid.gramophone.PlaylistFragmentTest
import com.kabouzeid.gramophone.PlaylistFragmentTest.Companion.NEW_PLAYLIST_MENU_POSITION
import com.kabouzeid.gramophone.R
import org.hamcrest.CoreMatchers
import java.lang.Thread.sleep

fun createPlaylist(playlistName: String) {
    openMenuAndClickByPosition(NEW_PLAYLIST_MENU_POSITION)

    onView(ViewMatchers.withText(R.string.new_playlist_title))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    onView(ViewMatchers.withHint(R.string.playlist_name_empty))
        .perform(ViewActions.typeText(playlistName))

    onView(ViewMatchers.withText(R.string.create_action)).perform(ViewActions.click())
    sleep(PlaylistFragmentTest.SLEEP_AFTER_CHANGE_RECYCLER)
}

fun openMenuAndClickByPosition(position: Int) {
    openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
    sleep(PlaylistFragmentTest.SLEEP_BETWEEN_CLICK_MENU)
    onData(CoreMatchers.anything()).atPosition(position).perform(ViewActions.click())
}