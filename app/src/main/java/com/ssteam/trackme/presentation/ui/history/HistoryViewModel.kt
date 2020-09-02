package com.ssteam.trackme.presentation.ui.history

import androidx.lifecycle.ViewModel
import com.ssteam.trackme.domain.repositories.ResultRepository
import com.ssteam.trackme.utils.OpenForTesting
import javax.inject.Inject
@OpenForTesting
class HistoryViewModel @Inject constructor(resultRepo: ResultRepository) : ViewModel(){
    val results = resultRepo.findAll()
}
