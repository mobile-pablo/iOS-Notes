package com.mobile.pablo.macrobenchmark.utils

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobile.pablo.macrobenchmark.ext.noteBenchmark
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalBaselineProfilesApi::class)
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baseLineRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baseLineRule.collectBaselineProfile(
        packageName = "com.mobile.pablo.iosnotes"
    ) {
        pressHome()
        startActivityAndWait()

        noteBenchmark {
            clickAddItemBtn()
        }
    }
}
