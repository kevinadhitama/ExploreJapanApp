package com.example.explorejapan.page.landing.ui.list.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.explorejapan.databinding.LandingListItemHeaderBinding
import com.example.explorejapan.datamodel.landing.LandingItem

class HeaderViewHolder(private val binding: LandingListItemHeaderBinding) :
    ViewHolder(binding.root) {

    fun bind(data: LandingItem) {
        binding.landingListItemHeaderTitle.text = data.title
    }
}