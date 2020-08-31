package com.ssteam.trackme.presentation.utils

import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.ssteam.trackme.domain.models.Location
import java.util.concurrent.TimeUnit


object Utils {
    fun isGpsEnable(context: Context) : Boolean{
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        return (gps_enabled || network_enabled)
    }
    fun getDurationText(millis: Long): String {
        return String.format(
            "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millis
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            )
        );
    }

    fun getDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val location1 = android.location.Location("location1")
        location1.latitude = lat1
        location1.longitude = lng1
        val location2 = android.location.Location("location2")
        location2.latitude = lat2
        location2.longitude = lng2

        return location1.distanceTo(location2) / 1000.0
    }

    fun drawStartLocation(map: GoogleMap, location: Location) {
        val latLng = LatLng(location.lat, location.lng)
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Start location")
        )
    }
    fun drawEndLocation(map: GoogleMap, location: Location) {
        val latLng = LatLng(location.lat, location.lng)
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("End location")
        )
    }
     fun drawRoute(map: GoogleMap,locations: List<Location>) {
        for (i in 0..locations.size-2) {
            val firstLocation = locations[i]
            val secondLocation = locations[i + 1]
            val polyLine = map.addPolyline(
                PolylineOptions().color(Color.RED)
                .add(
                    LatLng(firstLocation.lat, firstLocation.lng),
                    LatLng(secondLocation.lat, secondLocation.lng)
                )
            )
            polyLine.startCap = RoundCap()
            polyLine.endCap = RoundCap()
        }
    }

}
