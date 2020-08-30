package com.ssteam.trackme.domain.eventbusmodels

import com.ssteam.trackme.domain.models.Location


data class RecordingEvent(val status:RecordingStatusEvent, val locations: List<Location>, val distanceInKiloMeter: Float?=null, val speedInKiloMeterPerHour: Float?=null, val duration: Long?=null)
enum class RecordingStatusEvent{
    READY, RUNNING, PAUSED, STOPPED
}