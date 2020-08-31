package com.ssteam.trackme.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import com.ssteam.trackme.R
import com.ssteam.trackme.presentation.utils.Utils
import com.ssteam.trackme.presentation.ui.views.base.BaseLinearLayout
import kotlinx.android.synthetic.main.view_workout_result.view.*

class WorkoutResultView : BaseLinearLayout{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    override val layoutId: Int
        get() = R.layout.view_workout_result

    override fun setupUI() {

    }
    fun update(distanceInKiloMeter: Double, speedInKiloMeterPerHour: Double?, durationText: String){
        tvDistance.text = String.format("%.2f km", distanceInKiloMeter)
        if (speedInKiloMeterPerHour == null){
            tvSpeed.text = String.format("%s km/h", "--")
        }else{
            tvSpeed.text = String.format("%.2f km/h", speedInKiloMeterPerHour)
        }
        tvDuration.text = durationText
    }
}