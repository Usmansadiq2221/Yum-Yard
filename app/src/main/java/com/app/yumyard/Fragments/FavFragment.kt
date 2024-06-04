package com.app.yumyard.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.yumyard.Activities.MainActivity
import com.app.yumyard.Activities.MealDetailActivity
import com.app.yumyard.Adapters.FavMealAdapter
import com.app.yumyard.Models.Meal
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.callbacks.FavItemClick
import com.app.yumyard.databinding.FragmentFavBinding
import com.google.android.material.snackbar.Snackbar


class FavFragment : Fragment(), FavItemClick {

    private lateinit var binding: FragmentFavBinding
    private lateinit var favMvvm : HomeViewModel
    private lateinit var favMealAdapter: FavMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favMvvm = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavourites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favMealAdapter.differ.currentList.get(position)
                favMvvm.deleteMeal(meal)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        favMvvm.insertMeal(meal)
                    }
                ).show()

            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFav)

    }

    private fun prepareRecyclerView() {
        favMealAdapter = FavMealAdapter(this)
        binding.rvFav.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favMealAdapter
        }
    }

    private fun observeFavourites() {
        favMvvm.observeFavouriteMealsLiveData().observe(requireActivity(), Observer {meals->
            favMealAdapter.differ.submitList(meals)
        })
    }

    override fun onFavItemClick(model: Meal) {
        val intent = Intent(context, MealDetailActivity::class.java)
        intent.putExtra(HomeFragment.MEAL_ID, model.idMeal)
        intent.putExtra(HomeFragment.MEAL_NAME, model.strMeal)
        intent.putExtra(HomeFragment.MEAL_THUMB, model.strMealThumb)
        startActivity(intent)
    }

}