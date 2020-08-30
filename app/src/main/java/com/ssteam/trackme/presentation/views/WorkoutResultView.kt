package com.ssteam.trackme.presentation.views

import android.content.Context
import android.util.AttributeSet
import com.ssteam.trackme.R
import com.ssteam.trackme.presentation.utils.Utils
import com.ssteam.trackme.presentation.views.base.BaseLinearLayout
import kotlinx.android.synthetic.main.view_workout_result.view.*

class WorkoutResultView : BaseLinearLayout{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    override val layoutId: Int
        get() = R.layout.view_workout_result

    override fun setupUI() {

    }
    fun update(distanceInKiloMeter: Float?, speedInKiloMeterPerHour: Float?, duration: Long?){
        distanceInKiloMeter?.let{
            tvDistance.text = it.toString()
        }
        speedInKiloMeterPerHour?.let{
            tvSpeed.text = it.toString()
        }
        duration?.let {
            tvDuration.text =Utils.getDurationText(it)
        }
    }
}