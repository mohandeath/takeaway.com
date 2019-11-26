package com.takeaway.kiantestwork.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.takeaway.kiantestwork.RESPONSE_MOCK
import com.takeaway.kiantestwork.dto.Restaurant
import io.reactivex.Single
import javax.inject.Inject

/**
 * in real app this could be a network call or even seeding from a local database
 * but here i tried to load it from a local variable
 * (please notice that if the given json file was too large i would load it from a file)
 * using RxJava here looks like an over engeneering and it's not really necessary
 * but i've tried to show what it's like in a real world application!
 */
class RestaurantRepository @Inject constructor(
    private val gson: Gson
) {



    fun getRestaurantListDefaultSorting(): Single<List<Restaurant>> {
        val restaurantListType = object : TypeToken<ArrayList<Restaurant>>() { }.type
        val list:List<Restaurant> = gson.fromJson(RESPONSE_MOCK,restaurantListType)
        return Single.just(list.sortedBy { it.status == "open" })
    }

}