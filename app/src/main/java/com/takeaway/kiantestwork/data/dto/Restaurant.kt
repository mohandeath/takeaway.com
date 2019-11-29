package com.takeaway.kiantestwork.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * mapping jsons to POJO/DTO files
 * notice that there's an extra field that we are later
 * setting favorite state and map the outputs
 *
 */

@Entity(
    tableName = "restaurant"
)
data class Restaurant(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "sortValues")
    val sortingValues: SortingValue,

    @ColumnInfo(name = "favorite")
    val isFavorite: Boolean
)


@Entity(
    tableName = "sort_value"
)
data class SortingValue(

    @ColumnInfo(name = "bestMatch")
    val bestMatch: Float,

    @ColumnInfo(name = "newest")
    val newest: Float,
    @ColumnInfo(name = "bestMatch")
    val ratingAverage: Float,
    @ColumnInfo(name = "distance")
    val distance: Int,
    @ColumnInfo(name = "popularity")
    val popularity: Float,
    @ColumnInfo(name = "averageProductPrice")
    val averageProductPrice: Int,
    @ColumnInfo(name = "deliveryCosts")
    val deliveryCosts: Int,
    @ColumnInfo(name = "minCost")
    val minCost: Int

)