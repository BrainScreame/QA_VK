package com.kabouzeid.gramophone

import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kabouzeid.gramophone.PlaylistFragmentTest.Companion.DELETE_ITEM_POSITION_IN_MENU
import com.kabouzeid.gramophone.PlaylistFragmentTest.Companion.RENAME_ITEM_POSITION_IN_MENU
import com.kabouzeid.gramophone.ui.activities.MainActivity
import com.kabouzeid.gramophone.utils.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class PlaylistActivityTest {
    private val playlistName = "Music"

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        onView(withId(R.id.tabs))
            .perform(selectTabAtPosition(PlaylistFragmentTest.TAB_PLAYLIST_POSITION))

        createPlaylist(playlistName)
        clickItemInRecyclerByName(R.id.recycler_view, playlistName)
    }


    @After
    fun tearDown() {
        activityRule.scenario.close()
    }

    @Test
    fun renameAndDeletePlaylistTest() {
        val playlistNewName = "HAPPY"

        openMenuAndClickByPosition(RENAME_ITEM_POSITION_IN_MENU)
        onView(withHint(R.string.playlist_name_empty))
            .perform(ViewActions.replaceText(""))
        onView(withHint(R.string.playlist_name_empty))
            .perform(ViewActions.typeText(playlistNewName))
        onView(withText(R.string.rename_action)).perform(click())
        sleep(PlaylistFragmentTest.SLEEP_AFTER_CHANGE_RECYCLER)

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(playlistNewName))))

        //click toolbar back button
        onView(
            allOf(
                instanceOf(AppCompatImageButton::class.java),
                withParent(withId(R.id.toolbar))
            )
        ).perform(click())

        val playlistCount = getCountFromRecyclerView(R.id.recycler_view)

        checkNameItemInRecycler(R.id.recycler_view, playlistNewName)
        clickItemInRecyclerByName(R.id.recycler_view, playlistNewName)

        openMenuAndClickByPosition(DELETE_ITEM_POSITION_IN_MENU)
        onView(withText(R.string.delete_action)).perform(click())
        sleep(PlaylistFragmentTest.SLEEP_AFTER_CHANGE_RECYCLER)

        val currentPlaylistCount = getCountFromRecyclerView(R.id.recycler_view)

        assertEquals(playlistCount, currentPlaylistCount + 1)
    }

}