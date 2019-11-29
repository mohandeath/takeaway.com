package com.takeaway.kiantestwork.data.datasources

import com.takeaway.kiantestwork.data.dto.Restaurant

/**
 * in case if we have a RemoteDataSource it should implement this interface
 */
interface IDataSource {
    fun getRestaurantList(): List<Restaurant>
}