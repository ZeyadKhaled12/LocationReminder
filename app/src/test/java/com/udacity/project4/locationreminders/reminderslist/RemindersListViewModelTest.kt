package com.udacity.project4.locationreminders.reminderslist

import android.annotation.SuppressLint
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.ExpectFailure.assertThat
import com.udacity.project4.locationreminders.CoroutineMainRule
import com.udacity.project4.locationreminders.awaitValue
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var ruleCoroutines = CoroutineMainRule()


    private lateinit var repoReminder: FakeDataSource

    //Subject under test
    private lateinit var viewModel: RemindersListViewModel

    @Before
    fun setupViewModel() {
        repoReminder = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), repoReminder)
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("CheckResult")
    @Test
    fun remindersShowLoading() {
        rule.pauseDispatcher()

        viewModel.loadReminders()

        assertThat(viewModel.showLoading.awaitValue())

        rule.resumeDispatcher()

        assertThat(viewModel.showLoading.awaitValue())

    }

    @Test
    fun remindersLoad() = ruleCoroutines.runBlockingTest  {
        val reminder = ReminderDTO("My Store", "Pick Stuff", "Abuja", 6.454202, 7.599545)

        repoReminder.saveReminder(reminder)
        viewModel.loadReminders()

        assertThat(viewModel.remindersList.awaitValue())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun remindersLoadSnackValue() {
        ruleCoroutines.pauseDispatcher()

        repoReminder.errorReturn(true)

        viewModel.loadReminders()

        rule.resumeDispatcher()

        assertThat(viewModel.showSnackBar.awaitValue()).isEqualTo("Error getting reminders")
    }


    @After
    fun tearDown() {
        stopKoin()
    }

}


