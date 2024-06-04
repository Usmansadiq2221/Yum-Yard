package com.app.yumyard.Activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.yumyard.Fragments.HomeFragment
import com.app.yumyard.Models.Meal
import com.app.yumyard.R
import com.app.yumyard.RoomDb.MealDb
import com.app.yumyard.ViewModels.MealViewModel
import com.app.yumyard.ViewModels.MealViewModelFactory
import com.app.yumyard.databinding.ActivityMealDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso


class MealDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityMealDetailBinding

    lateinit var mealId: String
    lateinit var mealName: String
    lateinit var mealImage: String
    lateinit var youtubeLink: String

    private lateinit var mealMvvm: MealViewModel

    private var meal : Meal?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingCase()

        val mealDb = MealDb.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDb)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
//        mealMvvm = ViewModelProvider(this).get(MealViewModel::class.java)





        binding.back.setOnClickListener {
            finish()
        }

        getMealInfoFromIntent()
        setInfoInViews()

        observeMeal()


        mealMvvm.getMealDetail(mealId)
        observerMealDetialsLliveData()

        binding.btnYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("" + youtubeLink))
            startActivity(intent)
        }

        binding.btnAddToFav.setOnClickListener {
            mealMvvm.insertMeal(meal!!)
            val snackbar = Snackbar
                .make(
                    binding.mainLayout,
                    "Successfully added to favourite",
                    Snackbar.LENGTH_LONG
                )
            val view = snackbar.view
            val tv =
                view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            tv.setTextColor(Color.parseColor("#32CD32"))
            snackbar.show()
            binding.btnAddToFav.setIconResource(R.drawable.faverouit_icon)
        }

    }

    private fun loadingCase() {
        binding.btnAddToFav.visibility = View.GONE
        binding.scrollView.visibility = View.GONE
        binding.btnYoutube.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun observerMealDetialsLliveData() {
        mealMvvm.observeFullMealDetailsLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(meal: Meal) {
                binding.tvCategory.text = "Category: " + meal.strCategory
                binding.tvArea.text = "Area: " + meal.strArea
                binding.tvInstSteps.text = meal.strInstructions
                youtubeLink = meal.strYoutube!!
                this@MealDetailActivity.meal = meal
                responceCase()
            }

        })
    }

    private fun responceCase() {
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.scrollView.visibility = View.VISIBLE
        binding.btnYoutube.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun setInfoInViews() {
        Picasso.get()
            .load(mealImage)
            .placeholder(R.drawable.placeholderyumyard)
            .into(binding.imgMealDetail)

        binding.collapsingToolBar.title = mealName
//        if (mealMvvm.isMealExist(mealId)){
//            shortToast("MEAL EXIST")
//            binding.btnAddToFav.setIconResource(R.drawable.faverouit_icon)
//        }else{
//            binding.btnAddToFav.setIconResource(R.drawable.faverouit_outline_icon)
//            shortToast("not exists")
//        }

    }

    private fun getMealInfoFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealImage = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!


    }

    //popular item work...
    private fun observeMeal() {
        mealMvvm.observeFavouriteMealLiveData(mealId = mealId).observe(this
        ) { meal->
            if(meal!=null) {
                if (meal.idMeal != null) {
                    binding.btnAddToFav.setIconResource(R.drawable.faverouit_icon)
                } else {
                    binding.btnAddToFav.setIconResource(R.drawable.faverouit_outline_icon)
                }
            }
        }
    }
}