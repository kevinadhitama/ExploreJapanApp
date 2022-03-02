package com.example.explorejapan.page.landing.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.explorejapan.databinding.LandingListItemBinding
import com.example.explorejapan.databinding.LandingListItemHeaderBinding
import com.example.explorejapan.databinding.LandingListItemLoaderBinding
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.datamodel.landing.LandingItemType
import com.example.explorejapan.datamodel.landing.LandingItemType.CONTENT
import com.example.explorejapan.datamodel.landing.LandingItemType.HEADER
import com.example.explorejapan.datamodel.landing.LandingItemType.LOADER
import com.example.explorejapan.listener.ItemClickListener
import com.example.explorejapan.page.landing.ui.list.adapter.viewholder.ContentViewHolder
import com.example.explorejapan.page.landing.ui.list.adapter.viewholder.HeaderViewHolder
import com.example.explorejapan.page.landing.ui.list.adapter.viewholder.LoaderViewHolder

class LandingListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val dataSet: MutableList<LandingItem> = mutableListOf()
    private val loaderItem = LandingItem(title = "", type = LOADER)
    private var loaderPosition = -1
    var listener: ItemClickListener<LandingItem>? = null

    fun setDataSet(newDataSet: List<LandingItem>) {
        val prevSize = dataSet.size
        if (prevSize > 0) {
            dataSet.clear()
            notifyItemRangeRemoved(0, prevSize)
        }
        dataSet.addAll(newDataSet)
        notifyItemRangeInserted(0, newDataSet.size)
    }

    fun addDataSet(newDataSet: List<LandingItem>) {
        dataSet.addAll(newDataSet)
        notifyItemRangeInserted(dataSet.size - newDataSet.size, newDataSet.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (LandingItemType.from(viewType)) {
            HEADER -> HeaderViewHolder(
                LandingListItemHeaderBinding.inflate(inflater, parent, false)
            )
            CONTENT -> ContentViewHolder(
                LandingListItemBinding.inflate(inflater, parent, false)
            )
            LOADER -> LoaderViewHolder(
                LandingListItemLoaderBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataSet[position]
        when (LandingItemType.from(holder.itemViewType)) {
            HEADER -> (holder as HeaderViewHolder).bind(data)
            CONTENT -> (holder as ContentViewHolder).bind(position, data, listener)
            LOADER -> (holder as LoaderViewHolder)
        }
    }

    override fun getItemViewType(position: Int) = dataSet[position].type.value

    override fun getItemCount(): Int = dataSet.size

    fun showLoading() {
        hideLoading()
        dataSet.add(loaderItem)
        loaderPosition = dataSet.size - 1
        notifyItemInserted(loaderPosition)
    }

    fun hideLoading() {
        if (dataSet.remove(loaderItem)) {
            notifyItemRemoved(loaderPosition)
        }
    }
}