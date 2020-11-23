package com.cs518.comingday.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs518.comingday.R
import com.cs518.comingday.database.CategoryDatabase
import com.cs518.comingday.database.EventDatabase
import com.cs518.comingday.databinding.FragmentCategoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class CategoriesFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_category, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = CategoryDatabase.getInstance(application).categoryDatabaseDao
        val eventDataSource = EventDatabase.getInstance(application).eventDatabaseDao

        val viewModelFactory = CategoriesViewModelFactory(dataSource, eventDataSource)
        val categoriesViewModel = ViewModelProvider(this, viewModelFactory).get(CategoriesViewModel::class.java)

        binding.categoriesViewModel = categoriesViewModel

        val adapter = CategoryAdapter(CategoryClickListener { categoryId ->
            categoriesViewModel.onCategoryClicked(categoryId)
        })
        binding.categoryList.adapter = adapter
        binding.lifecycleOwner = this

        val deleteDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getText(R.string.category_delete_title_text))
            .setMessage(getText(R.string.category_delete_support_text))
            .setNeutralButton(getText(R.string.cancel)) {_, _ -> }
            .setPositiveButton(getText(R.string.delete)) {_, _ -> categoriesViewModel.doDelete()}
            .create()

        categoriesViewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Illegal category name.",
                    Snackbar.LENGTH_SHORT
                ).show()
                categoriesViewModel.doneShowSnackBar()
            }
        })

        categoriesViewModel.showDeleteConfirmation.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                deleteDialog.show()
                categoriesViewModel.doneShowDeleteConfirmation()
            }
        })

        categoriesViewModel.hideInputMethod.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                categoriesViewModel.doneHideInputMethod()
            }
        })

        categoriesViewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        val manager = LinearLayoutManager(context)
        binding.categoryList.layoutManager = manager

        return binding.root
    }
}