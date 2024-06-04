package com.app.yumyard.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.yumyard.Models.Meal
import com.app.yumyard.Models.MealList
import com.app.yumyard.RoomDb.MealDb
import com.app.yumyard.Utils.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDb: MealDb
) : ViewModel() {

    private var mealDetailLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealFullDetailsById(id).enqueue(object : Callback<MealList>{
            override fun onResponse(
                call: Call<MealList>,
                responce: Response<MealList>
            ) {
                if (responce.isSuccessful){
                    if (responce!=null){
                        mealDetailLiveData.value = responce.body()!!.meals[0]
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("ERRORGETTINGFULLMEALDETAILS", t.message.toString())
            }

        })
    }

    fun observeFullMealDetailsLiveData():LiveData<Meal>{
        return mealDetailLiveData
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

    fun observeFavouriteMealLiveData(mealId:String):LiveData<Meal>{
        return mealDb.mealDao().getMealById(mealId = mealId)
    }

    fun updateMeal(meal: Meal){
        viewModelScope.launch {
            mealDb.mealDao().updateMeal(meal)
        }
    }


}