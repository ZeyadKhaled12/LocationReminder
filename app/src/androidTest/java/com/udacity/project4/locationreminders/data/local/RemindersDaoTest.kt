package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun openDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun reminderSaveById() = runBlockingTest {
        val reminder = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)

        database.reminderDao().saveReminder(reminder)

        val result = database.reminderDao().getReminderById(reminder.id)

        assertThat(result as ReminderDTO, notNullValue())
        assertThat(result.id, `is`(reminder.id))
        assertThat(result.title, `is`(reminder.title))
        assertThat(result.description, `is`(reminder.description))
        assertThat(result.location, `is`(reminder.location))
        assertThat(result.latitude, `is`(reminder.latitude))
        assertThat(result.longitude, `is`(reminder.longitude))

    }

    @Test
    fun reminderGetById() = runBlockingTest {
        val reminder = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)


        database.reminderDao().saveReminder(reminder)

        assertThat(database.reminderDao().getReminders(), `is`(notNullValue()))
    }

    @Test
    fun reminderInsertAndDeleteById() = runBlockingTest {
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)

        database.reminderDao().saveReminder(rm)

        database.reminderDao().deleteAllReminders()

        assertThat(database.reminderDao().getReminders(), `is`(emptyList()))
    }

    @After
    fun closeDb() = database.close()

}