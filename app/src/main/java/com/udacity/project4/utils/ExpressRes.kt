package com.udacity.project4.utils

import androidx.test.espresso.idling.CountingIdlingResource

object ExpressRes {

    private const val RES = "GLOBAL"

    @JvmField
    val countRes = CountingIdlingResource(RES)

    fun increment() {
        countRes.increment()
    }

    fun decrement() {
        if (!countRes.isIdleNow) {
            countRes.decrement()
        }
    }

    inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
        ExpressRes.increment()
        return try {
            function()
        } finally {
            ExpressRes.decrement()
        }
    }
}