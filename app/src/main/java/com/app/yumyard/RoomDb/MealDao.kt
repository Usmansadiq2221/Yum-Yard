package com.app.yumyard.RoomDb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.yumyard.Models.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM MealInfo")
    fun getAllMeal():LiveData<List<Meal>>

    @Query("SELECT * FROM MealInfo WHERE idMeal=:mealId")
    fun getMealById(mealId:String):LiveData<Meal>
}