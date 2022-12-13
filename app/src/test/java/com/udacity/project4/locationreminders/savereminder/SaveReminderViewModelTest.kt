package com.udacity.project4.locationreminders.savereminder

import android.annotation.SuppressLint
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.R
import com.udacity.project4.locationreminders.CoroutineMainRule
import com.udacity.project4.locationreminders.awaitValue
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainRuleCr = CoroutineMainRule()



    private lateinit var remindersRepository: FakeDataSource


    private lateinit var viewModel: SaveReminderViewModel

    @Before
    fun insertViewModel() {
        remindersRepository = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)
    }



    @Test
    fun enterValidateData() {
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)

        Truth.assertThat(viewModel.validateEnteredData(rm)).isFalse()
        assertThat(viewModel.showSnackBarInt.awaitValue()).isEqualTo(R.string.err_select_location)
    }


    @SuppressLint("CheckResult")
    @Test
    fun reminderSave(){
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)
        mainRuleCr.pauseDispatcher()
        viewModel.saveReminder(rm)
        assertThat(viewModel.showLoading.awaitValue())
    }


    @Test
    fun enterValidateDataEmpity() {
        val rm = ReminderDTO("Egypt", "Food Restaurant", "Cairo", 30.14870719, 31.34233941)

        Truth.assertThat(viewModel.validateEnteredData(rm)).isFalse()
        assertThat(viewModel.showSnackBarInt.awaitValue()).isEqualTo(R.string.err_enter_title)
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}