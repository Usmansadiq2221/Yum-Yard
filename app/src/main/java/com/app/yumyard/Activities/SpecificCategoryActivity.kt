package com.app.yumyard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.yumyard.Adapters.SpecificCategoryMealAdapter
import com.app.yumyard.Fragments.HomeFragment
import com.app.yumyard.Models.PopularMealModel
import com.app.yumyard.RoomDb.MealDb
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.ViewModels.HomeViewModelFactory
import com.app.yumyard.callbacks.PopularItemCallback
import com.app.yumyard.databinding.ActivitySpecificCategoryBinding

class SpecificCategoryActivity : AppCompatActivity(), PopularItemCallback {

    private lateinit var binding:ActivitySpecificCategoryBinding

    val homeVM:HomeViewModel by lazy {
        val mealDb = MealDb.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDb)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    private lateinit var popularMealAdapter: SpecificCategoryMealAdapter

    private var mealName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpecificCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME).toString()
        popularMealAdapter = SpecificCategoryMealAdapter()

        binding.tvTitle.text = mealName

        prepareCategoryItemsRecyclerView()

        homeVM.getPopularItems(mealName)
        observeSpecificCategoryItems()


        binding.back.setOnClickListener {
            finish()
        }

    }

    private fun prepareCategoryItemsRecyclerView() {
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(this@SpecificCategoryActivity, 2)
            adapter = popularMealAdapter
        }
    }

    //popular item work...
    private fun observeSpecificCategoryItems() {
        homeVM.observePopularLiveData().observe(this
        ) { pMealList->
            popularMealAdapter.setMeals(list = pMealList as ArrayList<PopularMealModel>, this)
        }
    }

    override fun onItemClick(model: PopularMealModel) {
        var intent = Intent(this@SpecificCategoryActivity, MealDetailActivity::class.java)
        intent.putExtra(HomeFragment.MEAL_ID, model.idMeal)
        intent.putExtra(HomeFragment.MEAL_NAME, model.strMeal)
        intent.putExtra(HomeFragment.MEAL_THUMB, model.strMealThumb)
        startActivity(intent)
    }

    override fun onLongItemClick(mealId: String) {

    }
}