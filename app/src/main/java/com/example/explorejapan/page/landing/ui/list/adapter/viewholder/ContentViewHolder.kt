package com.example.explorejapan.page.landing.ui.list.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.explorejapan.databinding.LandingListItemBinding
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.listener.ItemClickListener
import com.example.explorejapan.utils.TextUtil

class ContentViewHolder(private val binding: LandingListItemBinding) :
    ViewHolder(binding.root) {

    fun bind(position: Int, data: LandingItem, listener: ItemClickListener<LandingItem>?) {
        Glide.with(binding.root.context).clear(binding.landingListItemImageViewBanner)
        data.imageUrl?.let {
            Glide.with(binding.root.context)
                .load(it)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.landingListItemImageViewBanner)
        }

        binding.landingListItemTextViewTitle.text = TextUtil.fromHtml(data.title)

        data.description?.let {
            binding.landingListItemTextViewDesc.text = TextUtil.fromHtml(it)
            binding.landingListItemTextViewDesc.visibility = View.VISIBLE
        } ?: run {
            binding.landingListItemTextViewDesc.text = ""
            binding.landingListItemTextViewDesc.visibility = View.GONE
        }

        binding.root.setOnClickListener {
            listener?.onItemClickListener(position, data)
        }
    }
}