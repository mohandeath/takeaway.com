package com.takeaway.kiantestwork.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.takeaway.kiantestwork.R
import com.takeaway.kiantestwork.data.dto.Restaurant
import com.takeaway.kiantestwork.data.dto.SortType
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestauranListAdapter(
    private val context: Context,
    var onClick: (Restaurant) -> Unit

) : RecyclerView.Adapter<RestauranListAdapter.RestaurantViewHolder>() {

    private var restaurants: MutableList<Restaurant> = ArrayList()

    private var sortType: SortType = SortType.DEFAULT_STATUS

    fun setItems(items: List<Restaurant>, sortType: SortType) {
        this.sortType = sortType
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


    //TODO explain in comment to
    private fun getSortValueLabe(sortType: SortType, restaurant: Restaurant) = when (sortType) {
        SortType.DEFAULT_STATUS, SortType.MINIMUM_COST -> "💰 Minimum Cost : €${restaurant.sortingValues.minCost} "
        SortType.NEWEST -> "🆕 Newest : ${restaurant.sortingValues.newest}"
        SortType.BEST_MATCH -> "🌯 Best Match : ${restaurant.sortingValues.bestMatch}"
        SortType.DISTANCE -> "🚚 Distance : ${restaurant.sortingValues.distance} Km"
        SortType.POPULARITY -> "📈 Popularity : ${restaurant.sortingValues.popularity}"
        SortType.AVERAGE_RATING -> "⭐️ Average Rate : ${restaurant.sortingValues.ratingAverage}"
        SortType.AVERAGE_PRICE -> "💵 Average Cost : ${restaurant.sortingValues.averageProductPrice}"
        SortType.DELIVERY_COST -> "📦 Delivery Cost € ${restaurant.sortingValues.deliveryCosts} "
    }

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(restaurant: Restaurant) {
            itemView.tvName.text = restaurant.name
            itemView.tvOpen.text = restaurant.status
            itemView.tvRate.rating = restaurant.sortingValues.ratingAverage
            itemView.tvDesc.text = getSortValueLabe(sortType, restaurant)
            itemView.imgLike.apply {
                if (restaurant.isFavorite)
                    setImageResource(R.drawable.ic_heart_filled)
                else setImageResource(R.drawable.ic_heart_outline)
            }

        }
    }
}
