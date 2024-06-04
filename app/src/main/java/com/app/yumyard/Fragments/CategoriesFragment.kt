package com.app.yumyard.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.yumyard.Activities.MainActivity
import com.app.yumyard.Activities.SpecificCategoryActivity
import com.app.yumyard.Adapters.CategoryFragMealAdapter
import com.app.yumyard.Models.Category
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.callbacks.CategoryItemCallback
import com.app.yumyard.databinding.FragmentCategoriesBinding


class CategoriesFragment : Fragment(), CategoryItemCallback {

    private lateinit var binding : FragmentCategoriesBinding

    private lateinit var categoryMvvm : HomeViewModel
    private lateinit var categoryMealAdapter: CategoryFragMealAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryMvvm = (activity as MainActivity).viewModel
        categoryMealAdapter = CategoryFragMealAdapter()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareCategoryItemsRecyclerView()

        categoryMvvm.getMealCategories()
        observeCategoryMeal()



    }

    private fun prepareCategoryItemsRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = categoryMealAdapter
        }
        binding.rvCategories.showShimmerAdapter()

    }

    //category work...
    private fun observeCategoryMeal(){
        categoryMvvm.observeCategoryMealLiveData().observe(viewLifecycleOwner,
            Observer<List<Category>> { cMealList->

                categoryMealAdapter.setMeals(list = cMealList as ArrayList<Category>, this)
                binding.rvCategories.hideShimmerAdapter()

            })
    }

    override fun onCategoryItemClick(model: Category) {
        var intent = Intent(context, SpecificCategoryActivity::class.java)
        intent.putExtra(HomeFragment.MEAL_NAME, model.strCategory)
        startActivity(intent)
    }
}