package com.app.yumyard.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.yumyard.Models.Meal
import com.app.yumyard.R
import com.app.yumyard.callbacks.FavItemClick
import com.app.yumyard.databinding.FavItemsBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class FavMealAdapter(val listener:FavItemClick) : RecyclerView.Adapter<FavMealAdapter.ViewHolder>() {


    inner class ViewHolder(val binding : FavItemsBinding) : RecyclerView.ViewHolder(binding.root)

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
            FavItemsBinding.inflate(
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

        Picasso.get().load(meal.strMealThumb).placeholder(R.drawable.placeholderyumyard).into(holder.binding.imgFavItem)

        holder.binding.mealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            listener.onFavItemClick(meal)
        }
    }


}