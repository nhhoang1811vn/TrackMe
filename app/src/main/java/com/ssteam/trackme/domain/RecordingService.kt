package com.ssteam.trackme.domain

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.google.android.gms.location.*
import com.ssteam.trackme.domain.eventbusmodels.RecordingEvent
import com.ssteam.trackme.domain.eventbusmodels.RecordingStatusEvent
import com.ssteam.trackme.domain.models.Location
import org.greenrobot.eventbus.EventBus
import java.util.*
@SuppressLint("MissingPermission")
//suppressLint because of ensure grant these permissions
class RecordingService : Service() {
    private lateinit var handlerThread : HandlerThread
    private lateinit var serviceHandler : ServiceHandler
    private var startTime : Long = 0
    private val timer = Timer()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest
    private var lastLocation : android.location.Location?=null
    private var fireReadyEvent = false
    private var locations = mutableListOf<Location>()
    private var totalDistanceInKm = 0f
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler.post {
            initLocationClient()
        }
        return START_STICKY
    }
    override fun onCreate() {
        super.onCreate()
        handlerThread = HandlerThread("RecordingService.HandlerThread")
        handlerThread.start()

        serviceHandler = ServiceHandler(handlerThread.looper)
        //initLocationClient()
    }
    private fun startRecording() {
        locations.clear()
        totalDistanceInKm = 0f
        startTime = System.currentTimeMillis()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val now = System.currentTimeMillis()
                val duration = now - startTime
                val speedInKiloMeterPerHour = lastLocation!!.speed
                locations.add(Location(lastLocation!!.latitude, lastLocation!!.longitude))
                val size = locations.size
                if (locations.size >= 2) {
                    //totalDistanceInKm += locations[size - 1].distanceTo(locations[size - 2]).toKm()
                }
                Log.d("RecordingService", System.currentTimeMillis().toString())
                EventBus.getDefault().post(
                    RecordingEvent(
                        RecordingStatusEvent.RUNNING,
                        locations = locations,
                        distanceInKiloMeter = totalDistanceInKm,
                        speedInKiloMeterPerHour = speedInKiloMeterPerHour,
                        duration = duration
                    )
                )
            }
        }, 0, 1000)
    }
    private fun pauseRecording(){
        ///timer.
    }
    private fun initLocationClient(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        createLocationCallback()
        startLocationUpdates()
    }
    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    private fun createLocationCallback(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                lastLocation = locationResult.lastLocation
                if (!fireReadyEvent){
                    fireReadyEvent = true
                    locations.add(Location(lastLocation!!.latitude, lastLocation!!.longitude))
                    EventBus.getDefault().post(RecordingEvent(RecordingStatusEvent.READY, locations))
                    startRecording()
                }
            }
        }
    }
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback, Looper.getMainLooper())
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    private class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {

        }
    }
}