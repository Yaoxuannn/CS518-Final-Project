package com.cs518.comingday.ui.eventdetail

import android.app.DatePickerDialog
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
import com.cs518.comingday.database.CategoryDatabase
import com.cs518.comingday.database.Event
import com.cs518.comingday.database.EventDatabase
import com.cs518.comingday.databinding.FragmentEventDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class EventDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentEventDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_event_detail, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = EventDatabase.getInstance(application).eventDatabaseDao
        val categoryDataSource = CategoryDatabase.getInstance(application).categoryDatabaseDao
        val arguments = EventDetailFragmentArgs.fromBundle(arguments)

        val viewModelFactory = EventDetailViewModelFactory(arguments.eventId, dataSource, categoryDataSource)
        val eventDetailViewModel = ViewModelProvider(this, viewModelFactory).get(EventDetailViewModel::class.java)

        binding.eventDetailViewModel = eventDetailViewModel
        binding.lifecycleOwner = this

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.JANUARY)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, _year, monthOfYear, dayOfMonth ->
            eventDetailViewModel.onDateSet(_year, monthOfYear+1, dayOfMonth)
        }, year, month, day)
        dpd.datePicker.minDate = System.currentTimeMillis() + (1000*3600*24)

        val adb = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.category_selection))
            .setNeutralButton(resources.getString(R.string.cancel)) {_, _ -> }
            .setPositiveButton(resources.getString(R.string.ok)) {_, _ -> }

        eventDetailViewModel.navigateToDashboard.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    EventDetailFragmentDirections.actionEventDetailFragmentToDashboardFragment()
                )
                eventDetailViewModel.doneNavigating()
            }
        })

        eventDetailViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) dpd.show()
        })

        eventDetailViewModel.showCategorySelector.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                adb.setSingleChoiceItems(eventDetailViewModel.categoryNames, eventDetailViewModel.checkedCategoryNameIdx ?: 0) { _, which ->
                    eventDetailViewModel.setCategory(which)
                }
                adb.show()
            }
        })



        return binding.root
    }


}