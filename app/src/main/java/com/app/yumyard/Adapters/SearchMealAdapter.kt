package com.app.yumyard.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.yumyard.Activities.MealDetailActivity
import com.app.yumyard.Fragments.HomeFragment
import com.app.yumyard.Models.Meal
import com.app.yumyard.R
import com.app.yumyard.databinding.SearchMealsItemBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class SearchMealAdapter(val context: Context) : RecyclerView.Adapter<SearchMealAdapter.ViewHolder>() {


    inner class ViewHolder(val binding : SearchMealsItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchMealsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = differ.currentList[position]

        Picasso.get().load(meal.strMealThumb).placeholder(R.drawable.placeholderyumyard).into(holder.binding.imgMeal)

        holder.binding.tvMealName.text = meal.strMeal
        holder.binding.tvLocation.text = meal.strArea
        holder.binding.tvCategory.text = meal.strCategory
        holder.binding.tvMealDesc.text = meal.strInstructions

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealDetailActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            context.startActivity(intent)
        }
    }


}