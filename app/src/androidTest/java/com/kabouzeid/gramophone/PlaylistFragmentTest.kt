package com.kabouzeid.gramophone

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kabouzeid.gramophone.ui.activities.MainActivity
import com.kabouzeid.gramophone.utils.*
import org.junit.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class PlaylistFragmentTest {

    companion object {
        const val TAB_PLAYLIST_POSITION = 4
        const val NEW_PLAYLIST_MENU_POSITION = 1
        const val ADD_TO_PLAYLIST_ITEM_POSITION_IN_MENU = 3
        const val RENAME_ITEM_POSITION_IN_MENU = 4
        const val DELETE_ITEM_POSITION_IN_MENU = 5
        const val SLEEP_AFTER_CHANGE_RECYCLER = 500L
        const val SLEEP_BETWEEN_CLICK_MENU = 300L
    }

    private val playlists = mutableListOf(
        "DORA",
//        "MORGENSHTERN",
//        "SLAWA MARLOW",
//        "FSFSDGSDFG",
//        "1332523",
//        "?]+_)(*&^%$#@!~`'"
    )

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(TAB_PLAYLIST_POSITION))
    }

    @After
    fun tearDown() {
        val playlistCount = getCountFromRecyclerView(R.id.recycler_view)
        for (i in 3 until playlistCount) {
            deletePlaylistByPosition(3)
            sleep(SLEEP_AFTER_CHANGE_RECYCLER)
        }
        activityRule.scenario.close()
    }

    @Test
    fun createPlaylistTest() {
        playlists.forEach {
            createPlaylist(it)
            checkNameItemInRecycler(R.id.recycler_view, it)
        }
    }

    @Test
    fun createPlaylistFromRecyclerItemTest() {
        val name = "KLAVA KOKA"
        val newPlaylistName = "Russian music"
        createPlaylist(name)
        checkNameItemInRecycler(R.id.recycler_view, name)
        createPlaylistFromRecyclerItem(name, newPlaylistName)
        checkNameItemInRecycler(R.id.recycler_view, newPlaylistName)
    }

    @Test
    fun renamePlaylistTest() {
        playlists.forEach {
            val changeName = "OMG $it AMAZING"
            createPlaylist(it)

            renamePlaylist(it, changeName)
            checkNameItemInRecycler(R.id.recycler_view, changeName)
        }
    }

    @Test
    fun deletePlayListTest() {
        playlists.forEach {
            createPlaylist(it)
            val playlistCount = getCountFromRecyclerView(R.id.recycler_view)
            deletePlaylistByName(it)
            assertEquals(playlistCount, getCountFromRecyclerView(R.id.recycler_view) + 1)
        }
    }

    @Test
    fun createAndRenamePlaylistTest() {
        val name = "Like music"
        val changeName = "Back $name"
        val playlistCount = getCountFromRecyclerView(R.id.recycler_view)

        createPlaylist(name)
        checkNameItemInRecycler(R.id.recycler_view, name)

        renamePlaylist(name, changeName)
        checkNameItemInRecycler(R.id.recycler_view, changeName)

        createPlaylist(name)
        val currentPlaylistCount = getCountFromRecyclerView(R.id.recycler_view)

        assertEquals(playlistCount + 2, currentPlaylistCount)
    }

    @Test
    fun creationIdenticalPlaylist() {
        val name = "New Playlist"
        createPlaylist(name)
        val playlistCount = getCountFromRecyclerView(R.id.recycler_view)
        createPlaylist(name)
        val currentPlaylistCount = getCountFromRecyclerView(R.id.recycler_view)
        assertEquals(playlistCount, currentPlaylistCount)

    }

    private fun createPlaylistFromRecyclerItem(
        clickItemPlaylistName: String,
        playlistName: String
    ) {
        clickChildItemInRecyclerByName(R.id.recycler_view, R.id.menu, clickItemPlaylistName)
        onData(CoreMatchers.anything()).atPosition(ADD_TO_PLAYLIST_ITEM_POSITION_IN_MENU)
            .perform(click())
        onView(withText(R.string.action_new_playlist)).check(matches(isDisplayed()))
        onView(withText(R.string.action_new_playlist)).perform(click())
        onView(withHint(R.string.playlist_name_empty)).perform(typeText(playlistName))
        onView(withText(R.string.create_action)).perform(click())
        sleep(SLEEP_AFTER_CHANGE_RECYCLER)
    }

    private fun renamePlaylist(playlistName: String, changeName: String) {
        clickChildItemInRecyclerByName(R.id.recycler_view, R.id.menu, playlistName)
        onData(CoreMatchers.anything()).atPosition(RENAME_ITEM_POSITION_IN_MENU)
            .perform(click())
        onView(withText(playlistName)).perform(replaceText(""))
        onView(withHint(R.string.playlist_name_empty)).perform(typeText(changeName))
        onView(withText(R.string.rename_action)).perform(click())
        sleep(SLEEP_AFTER_CHANGE_RECYCLER)
    }

    private fun deletePlaylistByName(playlistName: String) {
        clickChildItemInRecyclerByName(R.id.recycler_view, R.id.menu, playlistName)
        onData(CoreMatchers.anything()).atPosition(DELETE_ITEM_POSITION_IN_MENU)
            .perform(click())
        onView(withText(R.string.delete_action)).perform(click())
        sleep(SLEEP_AFTER_CHANGE_RECYCLER)
    }

    private fun deletePlaylistByPosition(position: Int) {
        clickChildItemInRecyclerByPosition(R.id.recycler_view, R.id.menu, position)
        onData(CoreMatchers.anything()).atPosition(DELETE_ITEM_POSITION_IN_MENU)
            .perform(click())
        onView(withText(R.string.delete_action)).perform(click())
        sleep(SLEEP_AFTER_CHANGE_RECYCLER)
    }
}
