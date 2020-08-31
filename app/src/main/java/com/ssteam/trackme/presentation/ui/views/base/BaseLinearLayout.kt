package com.ssteam.trackme.presentation.ui.views.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

abstract class BaseLinearLayout : LinearLayout {
    protected abstract val layoutId: Int
    protected var attrs : AttributeSet? = null

    constructor(context: Context) : super(context) {
        setLayout(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setLayout(context, attrs)
    }

    private fun setLayout(context: Context, attrs: AttributeSet?) {
        this.attrs = attrs
        LayoutInflater.from(context).inflate(layoutId, this)
        setupUI()
    }

    protected abstract fun setupUI()
}
