/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssteam.trackme.presentation

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ssteam.trackme.R
import com.ssteam.trackme.domain.Resource
import com.ssteam.trackme.presentation.ui.recording.RecordingFragment
import com.ssteam.trackme.presentation.ui.recording.RecordingViewModel
import com.ssteam.trackme.util.*
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class RecordingFragmentTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()
    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    private val navController = mock<NavController>()
    private val waitingStateLiveData = MutableLiveData<Boolean>()
    private val pauseStateLiveData = MutableLiveData<Boolean>()
    private val runningStateLiveData = MutableLiveData<Boolean>()
    private val saveResultStateLiveData = MutableLiveData<Resource<Boolean>>()
    private lateinit var viewModel: RecordingViewModel

    @Before
    fun init() {
        viewModel = mock(RecordingViewModel::class.java)
        `when`(viewModel.waitingState).thenReturn(waitingStateLiveData)
        `when`(viewModel.pauseState).thenReturn(pauseStateLiveData)
        `when`(viewModel.runningState).thenReturn(runningStateLiveData)
        `when`(viewModel.saveResultState).thenReturn(saveResultStateLiveData)

        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            RecordingFragment().also { fragment->
                fragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
            }
        }
        dataBindingIdlingResourceRule.monitorFragment(scenario)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.disableProgressBarAnimations()
        }
    }
    @Test
    fun testWaitingState(){
        waitingStateLiveData.postValue(true)
        onView(withId(R.id.viewActions)).check(matches(not(isDisplayed())))
        onView(withId(R.id.workoutResultView)).check(matches(not(isDisplayed())))

        /*waitingStateLiveData.postValue(false)
        onView(withId(R.id.viewActions)).check(matches(isDisplayed()))
        onView(withId(R.id.workoutResultView)).check(matches(isDisplayed()))*/
    }
    @Test
    fun testRunningState(){
        runningStateLiveData.postValue(true)
        onView(withId(R.id.viewActionRunning)).check(matches(isDisplayed()))
        onView(withId(R.id.viewActionPause)).check(matches(not(isDisplayed())))
    }
    @Test
    fun testPauseState(){
        pauseStateLiveData.postValue(true)
        onView(withId(R.id.viewActionRunning)).check(matches(not((isDisplayed()))))
        onView(withId(R.id.viewActionPause)).check(matches(isDisplayed()))
    }
    @Test
    fun testOnClickPause(){
        onView(withId(R.id.btnPause)).perform(click())
        verify(viewModel).pause()
    }
    @Test
    fun testOnClickResume(){
        onView(withId(R.id.btnResume)).perform(click())
        verify(viewModel).resume()
    }
    @Test
    fun testOnClickStop(){
        onView(withId(R.id.btnStop)).perform(click())
        verify(viewModel).stop()
    }
}