package com.takeaway.kiantestwork.data.repository

import com.takeaway.kiantestwork.data.datasources.local.LocalDataSource
import com.takeaway.kiantestwork.data.dto.Restaurant
import com.takeaway.kiantestwork.data.dto.SortType
import io.reactivex.Single
import javax.inject.Inject

/**
 * in real app this could be a network call or even seeding from a local database
 * but here i tried to load it from a local variable
 * (please notice that if the given json file was too large i would load it from a file)
 * using RxJava here may look like  over engeneering and it's not really necessary
 * but i've tried to show what it's like in a real world application!
 */
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

        //val result = favoriteRestaurants.union(restaurantList).toList()
        return favoriteRestaurants.map {
            it.union(restaurantList).toList().sortRestaurant(sortType)
        }
    }

}