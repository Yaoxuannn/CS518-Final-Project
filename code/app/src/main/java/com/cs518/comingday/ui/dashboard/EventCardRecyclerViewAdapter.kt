package com.cs518.comingday.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs518.comingday.database.Event
import com.cs518.comingday.databinding.ListItemEventCardBinding

class EventCardRecyclerViewAdapter(val cardClickListener: EventCardClickListener) :
    ListAdapter<Event, EventCardRecyclerViewAdapter.ViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, cardClickListener)
    }


    class ViewHolder private constructor(val binding: ListItemEventCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event, cardClickListener: EventCardClickListener) {
            binding.event = item
            binding.clickListener = cardClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemEventCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.eventId == newItem.eventId
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

}

class EventCardClickListener(val clickListener: (eventId: Long) -> Unit) {
    fun onClick(event: Event) = clickListener(event.eventId)
}