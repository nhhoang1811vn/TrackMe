package com.ssteam.trackme.data.db.mapper

import com.ssteam.trackme.data.db.entities.ResultEntity
import com.ssteam.trackme.domain.models.Result
import javax.inject.Inject

class ResultMapper @Inject constructor(val locationMapper: LocationMapper) : BaseMapper<Result, ResultEntity>(){
    override fun mapToEntity(model: Result): ResultEntity {
        return ResultEntity().apply {
            resultInfo.id = model.id
            resultInfo.distance = model.distance
            resultInfo.avgSpeed = model.avgSpeed
            resultInfo.duration = model.duration
            resultInfo.createdDate = model.createdDate
            locations = locationMapper.mapToEntities(model.locations.filterNotNull())
                .map {
                    it.resultId = resultInfo.id
                    it
                }
        }
    }

    override fun mapToModel(entity: ResultEntity): Result {
        return Result(entity.resultInfo.id,
            locationMapper.mapToModels(entity.locations).toMutableList(),
            entity.resultInfo.distance,
            entity.resultInfo.avgSpeed,
            entity.resultInfo.duration,
            entity.resultInfo.createdDate)
    }

}