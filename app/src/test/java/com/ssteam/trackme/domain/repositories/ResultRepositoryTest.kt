package com.ssteam.trackme.domain.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.ssteam.trackme.data.db.AppDatabase
import com.ssteam.trackme.data.db.dao.ResultDao
import com.ssteam.trackme.data.db.entities.ResultEntity
import com.ssteam.trackme.data.db.mapper.LocationMapper
import com.ssteam.trackme.data.db.mapper.ResultMapper
import com.ssteam.trackme.data.repositories.ResultRepositoryIpm
import com.ssteam.trackme.domain.Resource
import com.ssteam.trackme.domain.Status
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.util.InstantAppExecutors
import com.ssteam.trackme.util.TestUtil
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ResultRepositoryTest {
    private lateinit var SUT : ResultRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var db : AppDatabase
    @Mock
    private lateinit var resultDao : ResultDao

    var resultEntityCaptor = argumentCaptor<ResultEntity>()
    var listResultCaptor = argumentCaptor<Resource<List<Result>>>()

    @Before
    fun setUp() {
        db = Mockito.mock(AppDatabase::class.java)
        `when`(db.resultDao()).thenReturn(resultDao)
        SUT = ResultRepositoryIpm(db, InstantAppExecutors(), ResultMapper(LocationMapper()))
    }
    @Test
    fun testInsert(){
        SUT.insert(TestUtil.createResult(1.0,2.0,3))
        verify(resultDao).insertResult(resultEntityCaptor.capture())
        val actualResultEntity = resultEntityCaptor.firstValue
        assertThat(actualResultEntity.resultInfo.distance, `is`(1.0))
        assertThat(actualResultEntity.resultInfo.avgSpeed, `is`(2.0))
        assertThat(actualResultEntity.resultInfo.duration, `is`(3L))
        ///TODO
        //assert locations
    }
    @Test
    fun testFindAll(){
        val findAllLiveData = MutableLiveData<List<ResultEntity>>()
        `when`(resultDao.findAll()).thenReturn(findAllLiveData)

        val observer = mock<Observer<Resource<List<Result>>>>()
        SUT.findAll().observeForever(observer)

        findAllLiveData.postValue(mutableListOf(TestUtil.createResultEntity(1.0,2.0,3L,5)))
        verify(observer, times(2)).onChanged(listResultCaptor.capture())

        Assert.assertThat(listResultCaptor.firstValue.status, `is`(Status.LOADING))
        Assert.assertThat(listResultCaptor.secondValue.status, `is`(Status.SUCCESS))
    }
}