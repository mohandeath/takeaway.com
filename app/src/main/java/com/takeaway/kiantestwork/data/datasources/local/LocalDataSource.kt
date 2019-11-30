package com.takeaway.kiantestwork.data.datasources.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.takeaway.kiantestwork.RESPONSE_MOCK
import com.takeaway.kiantestwork.data.datasources.IDataSource
import com.takeaway.kiantestwork.data.dto.Restaurant
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val gson: Gson,
    private val dao: RestaurantDAO
) : IDataSource {
    override fun createRestaurant(restaurant: Restaurant): Single<Long> {
        return dao.insertRestaurant(restaurant)
    }

    override fun deleteRestaurant(restaurant: Restaurant): Single<Int> {
        return dao.removeRestaurant(restaurant)
    }

    override fun getRestaurantList(): List<Restaurant> {
        val restaurantListType = object : TypeToken<ArrayList<Restaurant>>() {}.type
        return gson.fromJson(RESPONSE_MOCK, restaurantListType)
    }

    override fun getFavoriteRestaurants(): Single<List<Restaurant>> {
        return dao.getRestaurants()
    }
}