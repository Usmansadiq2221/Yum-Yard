package com.app.yumyard.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.yumyard.Activities.MainActivity
import com.app.yumyard.Activities.MealDetailActivity
import com.app.yumyard.Activities.SpecificCategoryActivity
import com.app.yumyard.Adapters.CategoryMealAdapter
import com.app.yumyard.Adapters.PopularMealAdapter
import com.app.yumyard.Adapters.SwipeStackAdapter
import com.app.yumyard.Fragments.BottomSheet.MealBottomSheetFragment
import com.app.yumyard.Models.Category
import com.app.yumyard.Models.Meal
import com.app.yumyard.Models.PopularMealModel
import com.app.yumyard.R
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.callbacks.CategoryItemCallback
import com.app.yumyard.callbacks.PopularItemCallback
import com.app.yumyard.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import link.fls.swipestack.SwipeStack.SwipeStackListener


class HomeFragment : Fragment(), PopularItemCallback , CategoryItemCallback{
    lateinit var binding: FragmentHomeBinding;

    private lateinit var homeMvvm : HomeViewModel
    private lateinit var randomMeal : Meal
    private lateinit var popularMealAdapter: PopularMealAdapter
    private lateinit var categoryMealAdapter: CategoryMealAdapter

    companion object{
        const val MEAL_ID = "MEAL_ID"
        const val MEAL_NAME = "MEAL_NAME"
        const val MEAL_THUMB = "MEAL_THUMB"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = (activity as MainActivity).viewModel
        popularMealAdapter = PopularMealAdapter()
        categoryMealAdapter = CategoryMealAdapter()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        preparePopularItemsRecyclerView()
        prepareCategoryItemsRecyclerView()

        val mData = listOf(
            R.drawable.slide_one,
            R.drawable.slide_two,
            R.drawable.slide_three,
            R.drawable.slide_four,
            R.drawable.slide_one,
            R.drawable.slide_one,
            R.drawable.slide_two,
            R.drawable.slide_three,
            R.drawable.slide_four
        )


        binding.swipeStack.adapter = SwipeStackAdapter(mData, view.context)

        binding.swipeStack.setListener(object : SwipeStackListener{
            override fun onViewSwipedToLeft(position: Int) {

            }

            override fun onViewSwipedToRight(position: Int) {
            }

            override fun onStackEmpty() {
                binding.swipeStack.resetStack()
            }

        })


        homeMvvm.getRandomMeal()
        observeRAndomMeal()

//        homeMvvm.getPopularItems("Seafood")
//        observePopularItems()

        homeMvvm.getMealCategories()
        observeCategoryMeal()

        binding.randomImage.setOnClickListener {
            var intent = Intent(context, MealDetailActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }

        binding.randomImage.setOnLongClickListener {

            onLongItemClick(randomMeal.idMeal)
            return@setOnLongClickListener true
        }

        binding.search.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

    }

    private fun prepareCategoryItemsRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = categoryMealAdapter
        }
        binding.rvCategories.showShimmerAdapter()

    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopuralItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMealAdapter
        }
        binding.rvPopuralItems.showShimmerAdapter()
    }

    private fun observeRAndomMeal() {
        homeMvvm.observeRandomMEalLiveData().observe(viewLifecycleOwner, object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                Picasso.get().load(value.strMealThumb).placeholder(R.drawable.placeholderyumyard)
                    .into(binding.randomImage)
                binding.randomMealName.text = "" + value.strMeal
                randomMeal = value

            }

        })
    }


    //popular item work...
    private fun observePopularItems() {
        homeMvvm.observePopularLiveData().observe(viewLifecycleOwner
        ) { pMealList->
            popularMealAdapter.setMeals(list = pMealList as ArrayList<PopularMealModel>, this)
            binding.rvPopuralItems.hideShimmerAdapter()
        }
    }

    override fun onItemClick(model: PopularMealModel) {
        var intent = Intent(context, MealDetailActivity::class.java)
        intent.putExtra(MEAL_ID, model.idMeal)
        intent.putExtra(MEAL_NAME, model.strMeal)
        intent.putExtra(MEAL_THUMB, model.strMealThumb)
        startActivity(intent)
    }

    override fun onLongItemClick(mealId : String) {
        val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(mealId)
        mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
    }


    //category work...
    private fun observeCategoryMeal(){
        homeMvvm.observeCategoryMealLiveData().observe(viewLifecycleOwner,
            Observer<List<Category>> {cMealList->

                var randomNumber : Int = (0..cMealList.size-1).random()
                homeMvvm.getPopularItems(cMealList.get(randomNumber).strCategory + "")
                observePopularItems()
                categoryMealAdapter.setMeals(list = cMealList as ArrayList<Category>, this)
                binding.rvCategories.hideShimmerAdapter()

            })
    }

    override fun onCategoryItemClick(model: Category) {
        var intent = Intent(context, SpecificCategoryActivity::class.java)
        intent.putExtra(MEAL_NAME, model.strCategory)
        startActivity(intent)
    }
}