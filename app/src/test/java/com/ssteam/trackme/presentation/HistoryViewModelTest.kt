package com.ssteam.trackme.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.ssteam.trackme.domain.repositories.ResultRepository
import com.ssteam.trackme.presentation.ui.history.HistoryViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class HistoryViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = Mockito.mock(ResultRepository::class.java)
    private var viewModel = HistoryViewModel(repository)

    @Test
    fun testFindAll(){
        verify(repository).findAll()
    }
}