package com.ssteam.trackme.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class ResultInfoEntity{
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var distance: Double = 0.0
    var avgSpeed: Double? = null
    var duration: Long = 0L
    var createdDate: Date? = null
}