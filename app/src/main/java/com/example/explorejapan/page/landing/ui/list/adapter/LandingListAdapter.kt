package com.example.explorejapan.page.landing.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.explorejapan.databinding.LandingListItemBinding
import com.example.explorejapan.databinding.LandingListItemHeaderBinding
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.listener.ItemClickListener
import com.example.explorejapan.page.landing.ui.list.adapter.viewholder.ContentViewHolder
import com.example.explorejapan.page.landing.ui.list.adapter.viewholder.HeaderViewHolder

const val VIEW_TYPE_HEADER = 0
const val VIEW_TYPE_CONTENT = 1

class LandingListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val dataSet: MutableList<LandingItem> = mutableListOf()
    var listener: ItemClickListener<LandingItem>? = null

    fun setDataSet(newDataSet: List<LandingItem>) {
        val prevSize = dataSet.size
        dataSet.clear()
        notifyItemRangeRemoved(0, prevSize)
        dataSet.addAll(newDataSet)
        notifyItemRangeInserted(0, newDataSet.size)
    }

    fun addDataSet(newDataSet: List<LandingItem>) {
        dataSet.addAll(newDataSet)
        notifyItemRangeInserted(dataSet.size - newDataSet.size, newDataSet.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(
                    LandingListItemHeaderBinding.inflate(inflater, parent, false)
                )
            }
            else -> {
                ContentViewHolder(
                    LandingListItemBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataSet[position]
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                (holder as HeaderViewHolder).bind(data)
            }
            VIEW_TYPE_CONTENT -> {
                (holder as ContentViewHolder).bind(position, data, listener)
            }
        }
    }

    override fun getItemViewType(position: Int) =
        if (dataSet[position].isHeader) VIEW_TYPE_HEADER else VIEW_TYPE_CONTENT

    override fun getItemCount(): Int = dataSet.size

}