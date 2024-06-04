package com.app.yumyard.Fragments.BottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.app.yumyard.Activities.MainActivity
import com.app.yumyard.Activities.MealDetailActivity
import com.app.yumyard.Fragments.HomeFragment
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.databinding.FragmentMealBottomSheetBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var mealId: String? = null
    private lateinit var vModel : HomeViewModel

    private lateinit var binding: FragmentMealBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        vModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { vModel.getMealById(it) }

        observeBottomSheetViewModel()
    }

    private fun observeBottomSheetViewModel() {
        vModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal->

            Glide.with(context).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvLocation.text = meal.strArea
            binding.tvCategory.text = meal.strCategory
            binding.tvMealName.text = meal.strMeal

            binding.mealBottomSheet.setOnClickListener {
                val intent = Intent(context, MealDetailActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }

        })
    }

    companion object {
        @JvmStatic fun newInstance(mealId: String) =
                MealBottomSheetFragment().apply {
                    arguments = Bundle().apply {
                        putString(MEAL_ID, mealId)
                    }
                }
    }
}