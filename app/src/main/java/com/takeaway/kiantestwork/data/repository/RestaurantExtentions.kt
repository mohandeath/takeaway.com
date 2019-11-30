package com.takeaway.kiantestwork.data.repository

import com.takeaway.kiantestwork.data.dto.Restaurant
import com.takeaway.kiantestwork.data.dto.SortType


fun List<Restaurant>.sortRestaurant(
    sortType: SortType
): List<Restaurant> {
    val sortedByStatus =
        compareByDescending<Restaurant> { it.isFavorite }.thenByDescending { it.status == "open" }
            .thenByDescending { it.status == "order ahead" }
    return when (sortType) {

        SortType.DEFAULT_STATUS -> {
            sortedWith(sortedByStatus)
        }
        SortType.BEST_MATCH -> {
            sortedWith(sortedByStatus.thenByDescending { it.sortingValues.bestMatch })
        }
        SortType.AVERAGE_PRICE -> {
            sortedWith(
                sortedByStatus.thenByDescending { it.sortingValues.averageProductPrice }
            )
        }
        SortType.DELIVERY_COST -> {
            sortedWith(
                sortedByStatus.thenByDescending { it.sortingValues.deliveryCosts }
            )
        }
        SortType.NEWEST -> {
            sortedWith(
                sortedByStatus.thenBy { it.sortingValues.newest }
            )
        }
        SortType.DISTANCE -> {
            sortedWith(
                sortedByStatus.thenBy { it.sortingValues.distance }
            )
        }
        SortType.POPULARITY -> {
            sortedWith(
                sortedByStatus.thenByDescending { it.sortingValues.popularity }
            )
        }
        SortType.AVERAGE_RATING -> {
            sortedWith(
                sortedByStatus.thenByDescending { it.sortingValues.ratingAverage }
            )
        }
        SortType.MINIMUM_COST -> {
            sortedWith(
                sortedByStatus.thenBy { it.sortingValues.minCost }
            )
        }


    }
}