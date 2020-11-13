package com.cs518.comingday.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs518.comingday.R
import com.cs518.comingday.database.CategoryDatabase
import com.cs518.comingday.databinding.FragmentCategoryBinding

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
        val viewModelFactory = CategoriesViewModelFactory(dataSource)

        val categoriesViewModel = ViewModelProvider(this, viewModelFactory).get(CategoriesViewModel::class.java)
        binding.categoriesViewModel = categoriesViewModel

        val adapter = CategoryAdapter(CategoryClickListener { categoryId ->
            categoriesViewModel.onCategoryClicked(categoryId)
        })
        binding.categoryList.adapter = adapter
        binding.lifecycleOwner = this

        binding.addButton.setOnClickListener {
            categoriesViewModel.onAddButtonClicked()
        }

        binding.categoryNameConfirm.setOnClickListener {
            categoriesViewModel.onConfirmButtonClicked()
            categoriesViewModel.addNewCategory(binding.categoryNameEdit.text.toString())
        }


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