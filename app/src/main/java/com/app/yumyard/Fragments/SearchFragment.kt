package com.app.yumyard.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.yumyard.Activities.MainActivity
import com.app.yumyard.Adapters.SearchMealAdapter
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var vModel: HomeViewModel

    private lateinit var searchMealsAdapter: SearchMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vModel = (activity as MainActivity).viewModel




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareRecyclerViewForSearchMeals()

//        binding.btnSubmitSearch.setOnClickListener {
//            if (binding.etSearch.text.isNotEmpty()){
//                searchMeals(binding.etSearch.text.toString().trim())
//            }
//            else{
//                Snackbar.make(requireView(), "No Meal Found", Snackbar.LENGTH_SHORT)
//                    .setBackgroundTint(R.color.secondary)
//                    .setTextColor(R.color.primary).show()
//            }
//        }

        var searchJob: Job? = null
        binding.etSearch.addTextChangedListener { searchQuery->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                vModel.searchMeals(searchQuery.toString())
                binding.rvMeals.showShimmerAdapter()
            }
        }

        observeSearchMealsLiveData()

    }

    private fun observeSearchMealsLiveData() {
        vModel.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList->
            searchMealsAdapter.differ.submitList(mealsList)
            binding.rvMeals.hideShimmerAdapter()

        })
    }

    private fun searchMeals(searchQuery: String) {
        vModel.searchMeals(searchQuery)
    }

    private fun prepareRecyclerViewForSearchMeals() {
        searchMealsAdapter = SearchMealAdapter(context!!)
        binding.rvMeals.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchMealsAdapter

        }
    }

}