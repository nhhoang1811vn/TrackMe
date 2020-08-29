package com.ssteam.trackme.domain

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class RecordingService : Service() {
    private val timer = Timer()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        timer.schedule(object: TimerTask(){
            override fun run() {

            }
        },0,1000)
    }
}