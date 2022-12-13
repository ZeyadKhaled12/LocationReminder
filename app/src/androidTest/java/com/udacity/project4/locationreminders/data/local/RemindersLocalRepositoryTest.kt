package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)

@MediumTest
class RemindersLocalRepositoryTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var repoOfLocalReminders: RemindersLocalRepository

    private lateinit var database: RemindersDatabase

    @Before
    fun openDb() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        repoOfLocalReminders = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @Test
    fun reminderSaveById() = runBlocking {
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)

        repoOfLocalReminders.saveReminder(rm)
        val result = repoOfLocalReminders.getReminder(rm.id) as? Result.Success

        assertThat(result is Result.Success, `is`(true))
        result as Result.Success

        assertThat(result.data.title, `is`(rm.title))
        assertThat(result.data.description, `is`(rm.description))
        assertThat(result.data.latitude, `is`(rm.latitude))
        assertThat(result.data.longitude, `is`(rm.longitude))
        assertThat(result.data.location, `is`(rm.location))
    }


    @Test
    fun reminderDeleteById()= runBlocking {
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)
        repoOfLocalReminders.saveReminder(rm)
        repoOfLocalReminders.deleteAllReminders()

        val result = repoOfLocalReminders.getReminders()
        assertThat(result is Result.Success, `is`(true))
        result as Result.Success
        assertThat(result.data, `is` (emptyList()))
    }

    @Test
    fun reminderReturnByError() = runBlocking {
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)
        repoOfLocalReminders.saveReminder(rm)
        repoOfLocalReminders.deleteAllReminders()

        val result = repoOfLocalReminders.getReminder(rm.id)
        assertThat(result is Result.Error, `is`(true))
        result as Result.Error
        assertThat(result.message, `is`("Not found"))
    }

    @After
    fun cleanUp() {
        database.close()
    }
}