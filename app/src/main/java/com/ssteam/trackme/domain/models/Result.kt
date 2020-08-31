package com.ssteam.trackme.domain.models

data class Result(val locations: MutableList<Location?>, val distance: Double, val avgSpeed: Double?, val duration: Long)