package com.cs518.comingday.ui.eventdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cs518.comingday.R
import com.cs518.comingday.database.Event
import com.cs518.comingday.database.EventDatabase
import com.cs518.comingday.databinding.FragmentEventDetailBinding

class EventDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentEventDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_detail, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = EventDatabase.getInstance(application).eventDatabaseDao
        val arguments = EventDetailFragmentArgs.fromBundle(arguments)

        val viewModelFactory = EventDetailViewModelFactory(arguments.eventId, dataSource)
        val eventDetailViewModel = ViewModelProvider(this, viewModelFactory).get(EventDetailViewModel::class.java)

        binding.eventDetailViewModel = eventDetailViewModel

        eventDetailViewModel.navigateToDashboard.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    EventDetailFragmentDirections.actionEventDetailFragmentToDashboardFragment()
                )
                eventDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }


}