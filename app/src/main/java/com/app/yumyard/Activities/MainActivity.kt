package com.app.yumyard.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.app.yumyard.R
import com.app.yumyard.RoomDb.MealDb
import com.app.yumyard.ViewModels.HomeViewModel
import com.app.yumyard.ViewModels.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel:HomeViewModel by lazy {
        val mealDb = MealDb.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDb)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.btm_nav);
        val navController = Navigation.findNavController(this, R.id.mainFragment)

        NavigationUI.setupWithNavController(bottomNav, navController)




    }
}