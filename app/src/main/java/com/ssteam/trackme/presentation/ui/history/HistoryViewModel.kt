package com.ssteam.trackme.presentation.ui.history

import androidx.lifecycle.ViewModel
import com.ssteam.trackme.domain.repositories.ResultRepository
import javax.inject.Inject

class HistoryViewModel @Inject constructor(resultRepo: ResultRepository) : ViewModel(){
    val results = resultRepo.findAll()
}
