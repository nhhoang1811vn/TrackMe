package com.ssteam.trackme.presentation.ui.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssteam.trackme.domain.TrackingSession
import com.ssteam.trackme.domain.models.Location
import com.ssteam.trackme.domain.models.RecordingItem
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.domain.repositories.ResultRepository
import com.ssteam.trackme.presentation.utils.Utils
import javax.inject.Inject

class RecordingViewModel @Inject constructor(val trackingSession: TrackingSession, val resultRepo: ResultRepository): ViewModel() {
    private val recordingItems = mutableListOf<RecordingItem>()
    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean>
        get() = _isRunning

    fun start(){
        recordingItems.clear()
        _isRunning.value = true
        trackingSession.start()
    }
    fun pause(){
        _isRunning.value = false
        trackingSession.pause()
    }
    fun resume(){
        _isRunning.value = true
        trackingSession.resume()

    }
    fun stop() {
        trackingSession.stop()

        val locations = recordingItems.map { it.location }.toMutableList()
        val distance = getDistance()
        val avgSpeed = getAvgSpeed()
        val duration = getDuration()

        val result = Result(locations,distance,avgSpeed,duration)

    }
    private fun getDistance() : Double{
        var distance = 0.0
        if (recordingItems.isNotEmpty()){
            distance = recordingItems.last().distance
        }
        return distance
    }
    private fun getAvgSpeed() : Double?{
        val validSpeeds = recordingItems.filter { it.speed != null }
        return if (validSpeeds.isEmpty()){
            null
        }else{
            val sumSpeed = validSpeeds.map { it.speed!! }.sum()
            sumSpeed / validSpeeds.size
        }
    }
    private fun getDuration() : Long{
        return recordingItems.size * 1000L
    }


    fun addRecordingItem(item: RecordingItem) {
        recordingItems.add(item)
    }

    fun getDurationText(): String {
        return Utils.getDurationText(recordingItems.size*1000L)
    }

    fun getValidLocations(): List<Location> {
        return recordingItems.mapNotNull { it.location }
    }
}