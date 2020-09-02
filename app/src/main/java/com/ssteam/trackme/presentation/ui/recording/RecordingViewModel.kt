package com.ssteam.trackme.presentation.ui.recording

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.ssteam.trackme.domain.TrackingSession
import com.ssteam.trackme.domain.models.Location
import com.ssteam.trackme.domain.models.RecordingItem
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.domain.repositories.ResultRepository
import com.ssteam.trackme.presentation.utils.Utils
import com.ssteam.trackme.utils.OpenForTesting
import java.util.*
import javax.inject.Inject
@OpenForTesting
class RecordingViewModel @Inject constructor(val trackingSession: TrackingSession, val resultRepo: ResultRepository): ViewModel() {
    private val actionSave = MutableLiveData<Result>()
    var saveResultState = actionSave.switchMap {
        resultRepo.insert(it)
    }
    private final val recordingState = MutableLiveData<Int>()
    val waitingState = recordingState.map {
        it == STATE_WAITING
    }
    val runningState = recordingState.map {
        it == STATE_RUNNING
    }
    val pauseState = recordingState.map{
        it == STATE_PAUSED
    }
    private var recordingItems = mutableListOf<RecordingItem>()

    init {
        recordingState.value = STATE_WAITING
    }
    fun start(){
        recordingItems.clear()
        trackingSession.start()
    }
    fun setRunningState(){
        recordingState.value = STATE_RUNNING
    }
    fun pause(){
        recordingState.value = STATE_PAUSED
        trackingSession.pause()
    }
    fun resume(){
        recordingState.value = STATE_RUNNING
        trackingSession.resume()

    }
    fun stop() {
        trackingSession.stop()

        val locations = recordingItems.map { it.location }.toMutableList()
        val distance = getDistance()
        val avgSpeed = getAvgSpeed()
        val duration = getDuration()

        val result = Result(locations = locations,
            distance = distance,
            avgSpeed = avgSpeed,
            duration = duration,
            createdDate = Date()
            )
        saveResult(result)
    }
    private fun saveResult(result: Result){
        actionSave.value = result
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

    fun getDurationText(): String {
        return Utils.getDurationText(recordingItems.size*1000L)
    }

    fun getValidLocations(): List<Location> {
        return recordingItems.mapNotNull { it.location }
    }

    fun setRecordingItem(items: MutableList<RecordingItem>) {
        recordingItems = items
    }
    companion object{
        const val STATE_WAITING = 1
        const val STATE_RUNNING = 2
        const val STATE_PAUSED = 3
    }
}