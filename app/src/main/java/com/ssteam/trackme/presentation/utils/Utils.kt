package com.ssteam.trackme.presentation.utils

import android.location.Location
import java.util.concurrent.TimeUnit

object Utils {
    fun getDurationText(millis: Long) : String{
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    fun getDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val location1 = Location("location1")
        location1.latitude = lat1
        location1.longitude = lng1
        val location2 = Location("location2")
        location2.latitude = lat2
        location2.longitude = lng2

        return location1.distanceTo(location2) / 1000.0

    }
}