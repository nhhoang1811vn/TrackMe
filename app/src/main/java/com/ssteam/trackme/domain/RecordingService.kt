package com.ssteam.trackme.domain

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import com.google.android.gms.location.*
import com.ssteam.trackme.domain.eventbusmodels.MessageEvent
import com.ssteam.trackme.domain.models.Location
import com.ssteam.trackme.domain.models.RecordingItem
import com.ssteam.trackme.presentation.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@SuppressLint("MissingPermission")
//suppressLint because of ensure grant these permissions
class RecordingService : Service() {
    private lateinit var handlerThread : HandlerThread
    private lateinit var serviceHandler : ServiceHandler
    private var timer = Timer()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest
    private var lastLocation : android.location.Location?=null
    private var previousLocation : Location?=null
    private var locations = mutableListOf<Location>()
    private var totalDistanceInKm = 0.0
    private var recordingItems = mutableListOf<RecordingItem>()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    override fun onCreate() {
        super.onCreate()
        handlerThread = HandlerThread("RecordingService.HandlerThread")
        handlerThread.start()
        serviceHandler = ServiceHandler(handlerThread.looper)
        //serviceHandler.post { initLocationClient() }
        initLocationClient()
        startRecording()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    private var lookingForStartLocation = true
    private fun process(){
        if (lookingForStartLocation && lastLocation==null){
            return
        }
        lookingForStartLocation = false
        val speedInKiloMeterPerHour : Double? = lastLocation?.speed?.times(3.6)
        var location : Location? = null
        lastLocation?.let {
            location = Location(it.latitude, it.longitude)
            if (previousLocation != null){
                totalDistanceInKm += Utils.getDistance(
                    previousLocation!!.lat, previousLocation!!.lng,
                    location!!.lat, location!!.lng)
            }
            previousLocation = location
        }
        val recordingItem = RecordingItem(totalDistanceInKm,speedInKiloMeterPerHour,location)
        recordingItems.add(recordingItem)
        EventBus.getDefault().post(recordingItems)
    }
    private fun startRecording() {
        startLocationUpdates()
        recordingItems.clear()
        locations.clear()
        totalDistanceInKm = 0.0
        timer.schedule(object: TimerTask(){
            override fun run() {
                process()
            }
        },0,1000)
    }
    private fun pauseRecording(){
        timer.cancel()
        stopLocationUpdates()
    }
    private fun resumeRecording(){
        startLocationUpdates()
        timer = Timer()
        timer.schedule(object: TimerTask(){
            override fun run() {
                process()
            }
        },0,1000)
    }
    private fun initLocationClient(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        createLocationCallback()
    }
    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = REQUEST_INTERVAL
            fastestInterval = REQUEST_FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }
    private fun createLocationCallback(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                lastLocation = locationResult.lastLocation
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when(event.action){
            ACTION_PAUSE -> pauseRecording()
            ACTION_RESUME -> resumeRecording()
        }
    }
    companion object{
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val REQUEST_INTERVAL = 10000L
        const val REQUEST_FASTEST_INTERVAL = 5000L
    }
}