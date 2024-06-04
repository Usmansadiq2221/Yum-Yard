package com.app.yumyard.callbacks

import com.app.yumyard.Models.PopularMealModel

interface PopularItemCallback {

    fun onItemClick(model:PopularMealModel){

    }

    fun onLongItemClick(mealId: String)

}