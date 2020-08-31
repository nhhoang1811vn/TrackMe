package com.ssteam.trackme.data.db.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ssteam.trackme.data.db.converter.TimestampConverter
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