package com.app.yumyard.callbacks

import com.app.yumyard.Models.Meal

interface FavItemClick {

    fun onFavItemClick(model : Meal)

}