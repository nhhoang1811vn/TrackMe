package com.ssteam.trackme.data.db.mapper

import com.ssteam.trackme.data.db.entities.LocationEntity
import com.ssteam.trackme.domain.models.Location
import javax.inject.Inject

class LocationMapper @Inject constructor(): BaseMapper<Location, LocationEntity>() {
    override fun mapToEntity(model: Location): LocationEntity {
        return LocationEntity().apply {
            lat = model.lat
            lng = model.lng
        }
    }

    override fun mapToModel(entity: LocationEntity): Location {
        return Location(entity.lat, entity.lng)
    }
}