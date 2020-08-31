package com.ssteam.trackme.domain

import android.content.Context
import android.content.Intent
import com.ssteam.trackme.domain.eventbusmodels.MessageEvent
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class TrackingSessionIpm @Inject constructor(val context: Context) : TrackingSession {
    override fun start() {
        val intent = Intent(context, RecordingService::class.java)
        context.startService(intent)
    }

    override fun pause() {
        EventBus.getDefault().post(MessageEvent(RecordingService.ACTION_PAUSE))
    }

    override fun resume() {
        EventBus.getDefault().post(MessageEvent(RecordingService.ACTION_RESUME))
    }

    override fun stop() {
        val intent = Intent(context, RecordingService::class.java)
        context.stopService(intent)
    }
}