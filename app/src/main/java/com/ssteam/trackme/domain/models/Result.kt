package com.ssteam.trackme.domain.models

import java.util.*

data class Result(
    val id : String = UUID.randomUUID().toString(),
    val locations: MutableList<Location?>,
    val distance: Double,
    val avgSpeed: Double?,
    val duration: Long,
    val createdDate: Date?
    )