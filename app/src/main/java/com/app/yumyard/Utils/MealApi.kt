package com.app.yumyard.Utils

import com.app.yumyard.Models.MealCategoryListModel
import com.app.yumyard.Models.PopularMealListModels
import com.app.yumyard.Models.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php")
    fun getMealFullDetailsById(@Query("i") id:String) : Call<MealList>

    @GET("filter.php")
    fun getPopularMeals(@Query("c") fName:String) : Call<PopularMealListModels>

    @GET("categories.php")
    fun getMealCategories() : Call<MealCategoryListModel>

    @GET("search.php")
    fun getMealListBySearchName(@Query("s") mealName:String) : Call<MealList>
}