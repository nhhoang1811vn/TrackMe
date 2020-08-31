package com.ssteam.trackme.domain

import com.ssteam.trackme.domain.models.RecordingItem

interface TrackingSession {
    fun start()
    fun pause()
    fun resume()
    fun stop()
}