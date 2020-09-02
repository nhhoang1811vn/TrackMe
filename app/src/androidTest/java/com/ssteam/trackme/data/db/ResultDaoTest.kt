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

package com.ssteam.trackme.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ssteam.trackme.util.TestUtil
import com.ssteam.trackme.util.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResultDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndRead() {
        val result1 = TestUtil.createResultEntity(0.5, 1.0, 10,2)
        db.resultDao().insertResult(result1)
        val result2 = TestUtil.createResultEntity(10.0,20.0,200,3)
        db.resultDao().insertResult(result2)
        val loaded = db.resultDao().findAll().getOrAwaitValue()

        assertThat(loaded, notNullValue())
        assertThat(loaded[0].resultInfo.distance, `is`(0.5))
        assertThat(loaded[0].resultInfo.avgSpeed, `is`(1.0))
        assertThat(loaded[0].resultInfo.duration, `is`(10L))
        assertThat(loaded[0].locations, notNullValue())
        assertThat(loaded[0].locations.size, `is`(2))


        assertThat(loaded[1].resultInfo.distance, `is`(10.0))
        assertThat(loaded[1].resultInfo.avgSpeed, `is`(20.0))
        assertThat(loaded[1].resultInfo.duration, `is`(200L))
        assertThat(loaded[1].locations, notNullValue())
        assertThat(loaded[1].locations.size, `is`(3))
    }
}
