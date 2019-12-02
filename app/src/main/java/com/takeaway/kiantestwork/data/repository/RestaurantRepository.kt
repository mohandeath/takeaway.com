package com.takeaway.kiantestwork.data.repository

import com.takeaway.kiantestwork.data.datasources.local.LocalDataSource
import com.takeaway.kiantestwork.data.dto.Restaurant
import com.takeaway.kiantestwork.data.dto.SortType
import io.reactivex.Single
import javax.inject.Inject





class RestaurantRepository @Inject constructor(
    private val dataSource: LocalDataSource
) {

    fun addRestaurantToFavorites(restaurant: Restaurant): Single<Long> {
        restaurant.isFavorite = true
        return dataSource.createRestaurant(restaurant)
    }

    fun removeRestaurantFromFavorites(restaurant: Restaurant): Single<Int> {
        return dataSource.deleteRestaurant(restaurant)
    }

    fun filterRestaurantByName(sortType: SortType, query: String): Single<List<Restaurant>> {
        return getSortedRestaurantList(sortType).map {
            it.filter { item -> item.name.contains(query, ignoreCase = true) }
        }
    }

    fun getSortedRestaurantList(sortType: SortType): Single<List<Restaurant>> {

        val restaurantList = dataSource.getRestaurantList()

        val favoriteRestaurants = dataSource.getFavoriteRestaurants()

        return favoriteRestaurants.map {
            // notice that i've overrided the equal() in the Restaurant's model
            it.union(restaurantList).toList().sortRestaurant(sortType)
        }
    }

}