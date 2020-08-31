package com.ssteam.trackme.domain

interface TrackingSession {
    fun start()
    fun pause()
    fun resume()
    fun stop()
}