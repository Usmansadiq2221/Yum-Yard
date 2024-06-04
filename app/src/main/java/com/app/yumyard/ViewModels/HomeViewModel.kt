package com.app.yumyard.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.yumyard.Models.*
import com.app.yumyard.RoomDb.MealDb
import com.app.yumyard.Utils.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    val mealDb :MealDb
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<PopularMealModel>>()
    private var categoryMealLiveData = MutableLiveData<List<Category>>()

    private var favouriteMealsLiveData = mealDb.mealDao().getAllMeal()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    private var searchMealListLiveData = MutableLiveData<List<Meal>>()


    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(
                call: Call<MealList>,
                responce: Response<MealList>
            ) {
                if (responce.isSuccessful){
                    if (responce.body()!=null){
                        val randomMeal : Meal = responce.body()!!.meals.get(0)
                        randomMealLiveData.value = randomMeal
                    }else{
                        return
                    }
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.i("ERRORGETRANDOMMEAL", "Unable to get Random Meal");
            }
        })
    }

    fun observeRandomMEalLiveData():LiveData<Meal>{
        return randomMealLiveData
    }


    fun getPopularItems(category:String){
        RetrofitInstance.api.getPopularMeals(category).enqueue(object: Callback<PopularMealListModels>{
            override fun onResponse(
                call: Call<PopularMealListModels>,
                response: Response<PopularMealListModels>
            ) {
                if (response.isSuccessful){
                    if (response.body()!=null){
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }
            }

            override fun onFailure(call: Call<PopularMealListModels>, t: Throwable) {
                Log.d("ERRORGETTINGPOPULARITEMS", t.message.toString())
            }

        })
    }

    fun observePopularLiveData():LiveData<List<PopularMealModel>>{
        return popularItemsLiveData
    }

    fun getMealCategories(){
        RetrofitInstance.api.getMealCategories().enqueue(object : Callback<MealCategoryListModel>{
            override fun onResponse(
                call: Call<MealCategoryListModel>,
                response: Response<MealCategoryListModel>
            ) {
                if (response.isSuccessful){
                    if (response!=null){
                        categoryMealLiveData.value = response.body()!!.categories
                    }
                }
            }

            override fun onFailure(call: Call<MealCategoryListModel>, t: Throwable) {
                Log.d("ERRORGETTINGCATEGORIES", t.message.toString())
            }

        })
    }

    fun observeCategoryMealLiveData():LiveData<List<Category>>{
        return categoryMealLiveData
    }

    fun observeFavouriteMealsLiveData():LiveData<List<Meal>>{
        return favouriteMealsLiveData
    }



    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDb.mealDao().insertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDb.mealDao().deleteMeal(meal)
        }
    }

    fun getMealById(idString: String){
        RetrofitInstance.api.getMealFullDetailsById(id = idString).enqueue(object : Callback<MealList>{
            override fun onResponse(
                call: Call<MealList>,
                response: Response<MealList>
            ) {

                val meal = response.body()?.meals?.first()
                meal?.let {meal->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("FAILUREGETTINGSINGLEMEAL", t.message.toString())
            }

        })
    }

    fun observeBottomSheetMeal():LiveData<Meal> = bottomSheetMealLiveData

    fun searchMeals(searchQuery:String){
        RetrofitInstance.api.getMealListBySearchName(searchQuery)
            .enqueue(object : Callback<MealList>{
                override fun onResponse(call: Call<MealList>, responce: Response<MealList>) {
                    val mealList = responce.body()?.meals
                    mealList.let { meals->
                        searchMealListLiveData.postValue(meals)
                    }
                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.i("ERRORGETTINGSEARCHNAMEMEAL", t.message.toString())
                }

            })
    }

    fun observeSearchMealsLiveData() : LiveData<List<Meal>> = searchMealListLiveData

}