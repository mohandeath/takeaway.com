package com.takeaway.kiantestwork.data.datasources

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.takeaway.kiantestwork.RESPONSE_MOCK
import com.takeaway.kiantestwork.data.dto.Restaurant
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val gson: Gson
) : IDataSource {
    override fun getRestaurantList(): List<Restaurant>{
        val restaurantListType = object : TypeToken<ArrayList<Restaurant>>() {}.type
        return gson.fromJson(RESPONSE_MOCK, restaurantListType)
    }
}