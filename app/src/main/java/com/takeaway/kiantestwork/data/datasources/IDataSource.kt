package com.takeaway.kiantestwork.data.datasources

import com.takeaway.kiantestwork.data.dto.Restaurant
import io.reactivex.Single

/**
 * in case if we have a RemoteDataSource it should implement this interface
 */
interface IDataSource {
    fun getRestaurantList(): List<Restaurant>
    fun createRestaurant(restaurant: Restaurant): Single<Long>
    fun deleteRestaurant(restaurant: Restaurant): Single<Int>
    fun getFavoriteRestaurants(): Single<List<Restaurant>>

}