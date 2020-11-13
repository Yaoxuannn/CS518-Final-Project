package com.cs518.comingday.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cs518.comingday.R
import com.cs518.comingday.database.EventDatabase
import com.cs518.comingday.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = EventDatabase.getInstance(application).eventDatabaseDao

        val dashBoardViewModelFactory = DashboardViewModelFactory(dataSource)
        val dashboardViewModel = ViewModelProvider(this, dashBoardViewModelFactory).get(DashboardViewModel::class.java)

        binding.dashboardViewModel = dashboardViewModel

        val adapter = EventCardRecyclerViewAdapter(EventCardClickListener { eventId ->
            dashboardViewModel.onEventCardClicked(eventId)
        })

        binding.eventList.adapter = adapter
        binding.lifecycleOwner = this

        dashboardViewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        dashboardViewModel.navigateToEventDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToEventDetailFragment(it))
                dashboardViewModel.doneNavigating()
            }
        })

        val manager = GridLayoutManager(activity, 2)
        binding.eventList.layoutManager = manager


        return binding.root
    }
}