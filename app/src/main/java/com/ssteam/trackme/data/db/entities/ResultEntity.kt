package com.ssteam.trackme.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation


class ResultEntity {
    @Embedded
    var resultInfo = ResultInfoEntity()
    @Relation(parentColumn = "id", entityColumn = "resultId")
    var locations: List<LocationEntity> = mutableListOf()
}