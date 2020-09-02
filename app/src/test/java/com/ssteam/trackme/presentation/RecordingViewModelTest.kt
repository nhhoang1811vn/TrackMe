package com.ssteam.trackme.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.ssteam.trackme.domain.TrackingSession
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.domain.repositories.ResultRepository
import com.ssteam.trackme.presentation.ui.history.HistoryViewModel
import com.ssteam.trackme.presentation.ui.recording.RecordingViewModel
import org.hamcrest.CoreMatchers.any
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.lang.RuntimeException

@RunWith(JUnit4::class)
class RecordingViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private val repository = Mockito.mock(ResultRepository::class.java)
    private lateinit var trackingSessionTd : TrackingSessionTestDouble
    private lateinit var viewModel : RecordingViewModel

    @Before
    fun setUp() {
        trackingSessionTd = TrackingSessionTestDouble()
        viewModel = RecordingViewModel(trackingSessionTd,repository)
    }

    @Test
    fun testStartFunction(){
        viewModel.start()
        trackingSessionTd.verifyStartCall()
    }
    @Test
    fun testPauseFunction(){
        viewModel.pause()
        trackingSessionTd.verifyPauseCall()
    }
    @Test
    fun testResumeFunction(){
        viewModel.resume()
        trackingSessionTd.verifyResumeCall()
    }
    @Test
    fun testStopFunction(){
        viewModel.stop()
        trackingSessionTd.verifyStopCall()
    }
    class TrackingSessionTestDouble : TrackingSession{
        fun verifyStartCall(){
            if (startCount != 1){
                throw RuntimeException("invalid here, actual $startCount expect: 1")
            }
        }
        fun verifyPauseCall(){
            if (pauseCount != 1){
                throw RuntimeException("invalid here, actual $pauseCount expect: 1")
            }
        }
        fun verifyResumeCall(){
            if (resumeCount != 1){
                throw RuntimeException("invalid here, actual $resumeCount expect: 1")
            }
        }
        fun verifyStopCall(){
            if (stopCount != 1){
                throw RuntimeException("invalid here, actual $stopCount expect: 1")
            }
        }
        private var startCount = 0
        private var pauseCount = 0
        private var resumeCount = 0
        private var stopCount = 0


        override fun start() {
            startCount++
        }

        override fun pause() {
            pauseCount++
        }

        override fun resume() {
            resumeCount++
        }

        override fun stop() {
            stopCount++
        }
    }
}