package com.example.explorejapan.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.explorejapan.R
import com.example.explorejapan.databinding.WidgetErrorStateBinding

class ErrorStateWidget(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    lateinit var mBinding: WidgetErrorStateBinding
    var mListener: Listener? = null

    init {
        removeAllViews()
        val view: View =
            LayoutInflater.from(getContext()).inflate(R.layout.widget_error_state, this, false)
        addView(view)
        if (!isInEditMode) {
            mBinding = WidgetErrorStateBinding.bind(view)
            initListener()
            hide()
        }
    }

    private fun initListener() {
        mBinding.buttonRetry.setOnClickListener { v ->
            if (mListener != null) {
                mListener!!.onRetryClickListener()
            }
        }
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    interface Listener {

        fun onRetryClickListener()
    }
}