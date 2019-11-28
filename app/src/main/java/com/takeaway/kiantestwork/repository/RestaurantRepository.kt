package com.takeaway.kiantestwork.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.takeaway.kiantestwork.RESPONSE_MOCK
import com.takeaway.kiantestwork.dto.Restaurant
import com.takeaway.kiantestwork.dto.SortType
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
    private val gson: Gson
) {

    private fun List<Restaurant>.sortRestaurant(
        sortType: SortType
    ): List<Restaurant> {
        sortedWith(
            compareByDescending<Restaurant> { it.status == "open" }
                .thenByDescending { it.status == "order ahead" }
        )

        return when (sortType) {
            SortType.DEFAULT_STATUS -> {
                sortedByDescending { it.status == "closed" }

            }
            SortType.BEST_MATCH -> {
                sortedByDescending { it.sortingValues.bestMatch }
            }
            SortType.AVERAGE_PRICE -> {
                sortedByDescending { it.sortingValues.averageProductPrice }
            }
            SortType.DELIVERY_COST -> {
                sortedByDescending { it.sortingValues.deliveryCosts }

            }
            SortType.NEWEST -> {
                sortedByDescending { it.sortingValues.newest }
            }
            SortType.DISTANCE -> {
                sortedBy { it.sortingValues.distance }
            }
            SortType.POPULARITY -> {
                sortedByDescending { it.sortingValues.popularity }
            }
            SortType.AVERAGE_RATING -> {
                sortedByDescending { it.sortingValues.ratingAverage }
            }
            SortType.MINIMUM_COST -> {
                sortedBy { it.sortingValues.minCost }
            }


        }
    }



    //TODO move the json part to the local datasource
    fun getRestaurantListDefaultSorting(sortType: SortType): Single<List<Restaurant>> {
        val restaurantListType = object : TypeToken<ArrayList<Restaurant>>() {}.type
        val list: List<Restaurant> = gson.fromJson(RESPONSE_MOCK, restaurantListType)

        return Single.just(list.sortRestaurant(sortType))
    }

}