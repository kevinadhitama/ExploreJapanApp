package com.example.explorejapan.page.landing.ui.list.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.explorejapan.R
import com.example.explorejapan.databinding.LandingListItemLoadingPlaceholderBinding
import com.example.explorejapan.databinding.WidgetShimmeringLodingBinding

class LandingListShimmeringLoadingWidget(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    lateinit var mBinding: WidgetShimmeringLodingBinding
    lateinit var mListPlaceholder: MutableList<LandingListItemLoadingPlaceholderBinding>

    init {
        removeAllViews()
        val view: View = LayoutInflater.from(getContext())
            .inflate(R.layout.widget_shimmering_loding, this, false)
        addView(view)
        if (!isInEditMode) {
            mBinding = WidgetShimmeringLodingBinding.bind(view)
            initLoader()
        }
    }

    private fun initLoader() {
        mListPlaceholder = ArrayList()
        for (i in 0..9) {
            val binding: LandingListItemLoadingPlaceholderBinding = LandingListItemLoadingPlaceholderBinding.inflate(
                LayoutInflater.from(context),
                mBinding.shimmerViewContainer,
                true
            )
            mListPlaceholder.add(binding)
        }
    }

    fun show() {
        if (isShow()) return
        mListPlaceholder.forEach {
            it.shimmerViewContainer.startShimmer()
        }
        visibility = View.VISIBLE
    }

    fun hide() {
        if (!isShow()) return
        mListPlaceholder.forEach {
            it.shimmerViewContainer.stopShimmer()
        }
        visibility = View.GONE
    }

    private fun isShow(): Boolean {
        return visibility == View.VISIBLE
    }
}