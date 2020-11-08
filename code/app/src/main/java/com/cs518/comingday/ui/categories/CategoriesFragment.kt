package com.cs518.comingday.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs518.comingday.R
import com.cs518.comingday.databinding.FragmentDashboardBinding

class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        categoriesViewModel =
                ViewModelProvider(this).get(CategoriesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_category, container, false)


        return root
    }
}