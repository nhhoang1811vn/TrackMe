package com.ssteam.trackme.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = ResultInfoEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("resultId"), onDelete = CASCADE
    )
])
class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var lat: Double = 0.0
    var lng: Double = 0.0
    var resultId: String = ""
}