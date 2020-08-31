package com.ssteam.trackme.domain.repositories

import androidx.lifecycle.LiveData
import com.ssteam.trackme.domain.Resource
import com.ssteam.trackme.domain.models.Result

interface ResultRepository {
    fun findAll() : LiveData<Resource<List<Result>>>
    fun insert(model: Result) : LiveData<Resource<Boolean>>
}