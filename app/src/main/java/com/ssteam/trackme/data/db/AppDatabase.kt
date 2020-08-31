package com.ssteam.trackme.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssteam.trackme.data.db.converter.TimestampConverter
import com.ssteam.trackme.data.db.dao.ResultDao
import com.ssteam.trackme.data.db.entities.LocationEntity
import com.ssteam.trackme.data.db.entities.ResultInfoEntity

@Database(entities = [ResultInfoEntity::class, LocationEntity::class], version = 1)
@TypeConverters(TimestampConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao
}