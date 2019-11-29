package com.takeaway.kiantestwork.data.dto


/**
 * mapping jsons to POJO/DTO files
 * notice that there's an extra field that we are later
 * setting favorite state and map the outputs
 *
 */
data class Restaurant(
    val name: String,
    val status: String,
    val sortingValues: SortingValue,
    val isFavorite:Boolean = false
)

data class SortingValue(
    val bestMatch:Float,
    val newest:Float,
    val ratingAverage:Float,
    val distance:Int,
    val popularity:Float,
    val averageProductPrice:Int,
    val deliveryCosts:Int,
    val minCost:Int

)