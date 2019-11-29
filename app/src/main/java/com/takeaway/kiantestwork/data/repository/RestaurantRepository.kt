package com.takeaway.kiantestwork.data.repository

import android.content.SharedPreferences
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
    private val dataSource: LocalDataSource,
    private val sharedPreferences: SharedPreferences
) {

    fun addRestaurantToFavorites(restaurant: Restaurant){

    }

    fun getRestaurantListDefaultSorting(sortType: SortType): Single<List<Restaurant>> {

        return Single.just(dataSource.getRestaurantList().sortRestaurant(sortType))
    }

}