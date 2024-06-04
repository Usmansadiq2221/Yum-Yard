package com.app.yumyard.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.yumyard.Fragments.CategoriesFragment
import com.app.yumyard.Models.Category
import com.app.yumyard.R
import com.app.yumyard.callbacks.CategoryItemCallback
import com.app.yumyard.databinding.CategoryFragItemsBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class CategoryFragMealAdapter() : RecyclerView.Adapter<CategoryFragMealAdapter.ViewHolder>() {

    private var list = ArrayList<Category>()
    private lateinit var listener: CategoryItemCallback


    fun setMeals(list: ArrayList<Category>, listener: CategoriesFragment?){
        this.list = list
        this.listener = listener!!
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryFragItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(list.get(position).strCategoryThumb).placeholder(R.drawable.placeholderyumyard).into(holder.binding.imgMeal)
        holder.binding.mealName.text = list.get(position).strCategory

        holder.itemView.setOnClickListener {
            listener.onCategoryItemClick(list.get(position))
        }

    }



    class ViewHolder(var binding: CategoryFragItemsBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}