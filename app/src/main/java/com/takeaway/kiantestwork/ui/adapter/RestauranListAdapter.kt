package com.takeaway.kiantestwork.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.takeaway.kiantestwork.R
import com.takeaway.kiantestwork.dto.Restaurant
import com.takeaway.kiantestwork.dto.SortType
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestauranListAdapter(
    private val context: Context,
    var onClick: (Restaurant) -> Unit

) : RecyclerView.Adapter<RestauranListAdapter.RestaurantViewHolder>() {

    private var restaurants: MutableList<Restaurant> = ArrayList()

    private var sortType: SortType = SortType.DEFAULT_STATUS

    fun setItems(items: List<Restaurant>, sortType: SortType) {
        this.restaurants.clear()
        this.restaurants.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = restaurants[position]
        holder.bindView(item)
        holder.itemView.imgLike.setOnClickListener { onClick(item) }
    }


    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindView(restaurant: Restaurant) {
            itemView.tvName.text = restaurant.name
            itemView.tvOpen.text = restaurant.status
            itemView.tvRate.rating = restaurant.sortingValues.ratingAverage
            itemView.tvDesc.text = "€ ${restaurant.sortingValues.averageProductPrice}"
            itemView.imgLike.apply {
                if (restaurant.isFavorite)
                    setImageResource(R.drawable.ic_heart_filled)
                else setImageResource(R.drawable.ic_heart_outline)
            }

        }
    }
}
