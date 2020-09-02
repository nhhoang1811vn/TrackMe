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
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ssteam.trackme.R
import com.ssteam.trackme.domain.Resource
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.presentation.ui.history.HistoryFragment
import com.ssteam.trackme.presentation.ui.history.HistoryViewModel
import com.ssteam.trackme.util.*
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {
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
    private val resultsLiveData = MutableLiveData<Resource<List<Result>>>()
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun init() {
        viewModel = mock(HistoryViewModel::class.java)
        `when`(viewModel.results).thenReturn(resultsLiveData)
        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            HistoryFragment().also {fragment->
                fragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
                fragment.appExecutors = countingAppExecutors.appExecutors
            }
        }
        dataBindingIdlingResourceRule.monitorFragment(scenario)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun testLoading() {
        resultsLiveData.postValue(Resource.loading(null))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.tvNoData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultList)).check(matches(not(isDisplayed())))
    }
    @Test
    fun testEmptyData(){
        resultsLiveData.postValue(Resource.success(mutableListOf()))
        onView(withId(R.id.tvNoData)).check(matches(isDisplayed()))
        onView(withId(R.id.resultList)).check(matches(not(isDisplayed())))
    }
    @Test
    fun testHasData(){
        resultsLiveData.postValue(Resource.success(TestUtil.createResults(5)))
        onView(withId(R.id.tvNoData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultList)).check(matches(isDisplayed()))
    }
    @Test
    fun testCorrectDataBinding(){
        resultsLiveData.postValue(Resource.success(TestUtil.createResults(3)))
        onView(listMatcher().atPosition(0)) .check(matches(isDisplayed()))
        onView(listMatcher().atPosition(1)) .check(matches(isDisplayed()))
        onView(listMatcher().atPosition(2)) .check(matches(isDisplayed()))
    }
    @Test
    fun nulledResultList(){
        resultsLiveData.postValue(null)
        onView(listMatcher().atPosition(0)) .check(doesNotExist())
    }
    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.resultList)
    }
}