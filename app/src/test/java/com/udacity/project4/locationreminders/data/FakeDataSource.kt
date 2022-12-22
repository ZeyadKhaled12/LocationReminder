package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result


class FakeDataSource(var listOfReminders: MutableList<ReminderDTO>? = mutableListOf())
    : ReminderDataSource {


    private var errorReturn = false


    override suspend fun getReminders(): Result<List<ReminderDTO>> {

        if (errorReturn) {
            return Result.Error(
                "Get Error Reminder"
            )
        }

        listOfReminders?.let { return Result.Success(it) }


        return Result.Error("not found!")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        listOfReminders?.add(reminder)
    }

    fun errorReturn(value: Boolean) {
        errorReturn = value
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        val reminder = listOfReminders?.find { reminderDTO ->
            reminderDTO.id == id
        }

        return when {
            errorReturn -> {
                Result.Error("not found!")
            }

            reminder != null -> {
                Result.Success(reminder)
            }
            else -> {
                Result.Error("not found!")
            }
        }
    }

    override suspend fun deleteAllReminders() {
        listOfReminders?.clear()
    }

}