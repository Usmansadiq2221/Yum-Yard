package com.app.yumyard.callbacks

import com.app.yumyard.Models.Category

interface CategoryItemCallback {

    fun onCategoryItemClick(model:Category)
}