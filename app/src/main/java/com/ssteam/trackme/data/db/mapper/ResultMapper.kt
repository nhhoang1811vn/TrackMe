package com.ssteam.trackme.data.db.mapper

import com.ssteam.trackme.data.db.entities.ResultEntity
import com.ssteam.trackme.domain.models.Result
import javax.inject.Inject

class ResultMapper @Inject constructor(val locationMapper: LocationMapper) : BaseMapper<Result, ResultEntity>(){
    override fun mapToEntity(model: Result): ResultEntity {
        return ResultEntity().apply {
            resultInfo.distance = model.distance
            resultInfo.avgSpeed = model.avgSpeed
            resultInfo.duration = model.duration

            locations = locationMapper.mapToEntities(model.locations.filterNotNull())
                .map {
                    it.resultId = resultInfo.id
                    it
                }
        }
    }

    override fun mapToModel(entity: ResultEntity): Result {

        return Result(locationMapper.mapToModels(entity.locations).toMutableList(),entity.resultInfo.distance, entity.resultInfo.avgSpeed, entity.resultInfo.duration)
    }

}