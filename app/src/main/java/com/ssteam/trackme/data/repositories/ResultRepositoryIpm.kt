package com.ssteam.trackme.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ssteam.trackme.data.db.AppDatabase
import com.ssteam.trackme.data.db.mapper.ResultMapper
import com.ssteam.trackme.domain.AppExecutors
import com.ssteam.trackme.domain.Resource
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.domain.repositories.ResultRepository
import java.lang.Exception
import javax.inject.Inject

class ResultRepositoryIpm @Inject constructor(appDatabase: AppDatabase, val appExecutors: AppExecutors, val mapper: ResultMapper) : ResultRepository {
    private val dao = appDatabase.resultDao()
    override fun findAll(): LiveData<Resource<List<Result>>> {
        val liveData = MediatorLiveData<Resource<List<Result>>>()
        liveData.value = Resource.loading()
        liveData.addSource(dao.findAll()){
            liveData.value = Resource.success(mapper.mapToModels(it))
        }
        return liveData
    }

    override fun insert(model: Result): LiveData<Resource<Boolean>> {
        val liveData = MediatorLiveData<Resource<Boolean>>()
        liveData.value = Resource.loading()
        try{
            appExecutors.diskIO().execute{
                dao.insertResult(mapper.mapToEntity(model))
                appExecutors.mainThread().execute{
                    //liveData.value = Resource.success(true)
                }
            }
        }catch (ex: Exception){
            liveData.value = Resource.error(ex.message!!,false)
        }
        return liveData
    }
}