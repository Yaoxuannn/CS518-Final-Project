package com.cs518.comingday.ui.eventdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cs518.comingday.R
import com.cs518.comingday.database.Event
import com.cs518.comingday.database.EventDatabase
import com.cs518.comingday.databinding.FragmentEventDetailBinding

class EventDetailFragment : Fragment() {

    private lateinit var viewModel: EventDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentEventDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_detail, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = EventDatabase

        return binding.root
    }


}