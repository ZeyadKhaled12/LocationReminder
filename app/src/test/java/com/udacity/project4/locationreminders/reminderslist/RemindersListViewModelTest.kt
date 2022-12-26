@file:Suppress("DEPRECATION")

package com.udacity.project4.locationreminders.reminderslist


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.CoroutineMainRule
import com.udacity.project4.locationreminders.savereminder.awaitValue
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.DelayController
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import kotlin.coroutines.ContinuationInterceptor


@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var ruleCoroutines = CoroutineMainRule()


    private lateinit var repoReminder: FakeDataSource


    private lateinit var viewModel: RemindersListViewModel

    @Before
    fun setupViewModel() {

        repoReminder = FakeDataSource()


        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), repoReminder)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun remindersShowLoading() {


        (ruleCoroutines.coroutineContext[ContinuationInterceptor]!! as DelayController).pauseDispatcher()


        viewModel.loadReminders()


        assertThat(viewModel.showLoading.awaitValue()).isTrue()


        ruleCoroutines.resumeDispatcher()


        assertThat(viewModel.showLoading.awaitValue()).isFalse()

    }

    @Test
    fun remindersLoad() = ruleCoroutines.runBlockingTest  {

        val reminder = ReminderDTO("My Store", "Pick Stuff", "Abuja", 6.454202, 7.599545)

        repoReminder.saveReminder(reminder)
        viewModel.loadReminders()


        assertThat(viewModel.remindersList.awaitValue()).isNotEmpty()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun remindersLoadSnackValue() {

        ruleCoroutines.pauseDispatcher()

        repoReminder.errorReturn(true)


        viewModel.loadReminders()


        ruleCoroutines.resumeDispatcher()

        assertThat(viewModel.showSnackBar.awaitValue()).isEqualTo("Error getting reminders")
    }


    @After
    fun tearDown() {
        stopKoin()
    }

}


