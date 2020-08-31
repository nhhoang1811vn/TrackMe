package com.ssteam.trackme.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ssteam.trackme.data.db.entities.LocationEntity
import com.ssteam.trackme.data.db.entities.ResultEntity
import com.ssteam.trackme.data.db.entities.ResultInfoEntity

@Dao
interface ResultDao {
    @Insert
    fun insert(entity: ResultInfoEntity)

    @Insert
    fun insert(entities: List<LocationEntity>)

    @Transaction
    fun insertResult(result: ResultEntity){
        insert(result.resultInfo)
        insert(result.locations)
    }

    @Query("SELECT * FROM ResultInfoEntity")
    fun findAll(): LiveData<List<ResultEntity>>
}