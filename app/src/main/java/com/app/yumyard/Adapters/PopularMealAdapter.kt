package com.app.yumyard.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.yumyard.Activities.SpecificCategoryActivity
import com.app.yumyard.Models.PopularMealModel
import com.app.yumyard.R
import com.app.yumyard.callbacks.PopularItemCallback
import com.app.yumyard.databinding.PopularItemsBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class PopularMealAdapter() : RecyclerView.Adapter<PopularMealAdapter.ViewHolder>() {

    private var list = ArrayList<PopularMealModel>()
    private lateinit var listener: PopularItemCallback

    fun setMeals(list: ArrayList<PopularMealModel>, listener: PopularItemCallback?){
        this.list = list
        this.listener = listener!!
        notifyDataSetChanged()
    }

    fun setMeals(list: ArrayList<PopularMealModel>, listener: SpecificCategoryActivity?){
        this.list = list
        this.listener = listener!! as PopularItemCallback
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(list.get(position).strMealThumb).placeholder(R.drawable.placeholderyumyard).into(holder.binding.imgPopularItem)
        holder.binding.mealName.text = list.get(position).strMeal

        holder.itemView.setOnClickListener {
            listener.onItemClick(list.get(position))
        }

        holder.itemView.setOnLongClickListener {
            listener.onLongItemClick(list.get(position).idMeal)
            return@setOnLongClickListener true
        }

    }



    class ViewHolder(var binding: PopularItemsBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}