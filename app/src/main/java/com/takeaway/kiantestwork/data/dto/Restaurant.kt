package com.takeaway.kiantestwork.data.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * mapping jsons to POJO/DTO files
 * notice that there's an extra field that we are later
 * setting favorite state and map the outputs
 *  overrides the equals() and hashcode() to avoid adding duplicate
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

    @Embedded
    val sortingValues: SortingValue,

    @ColumnInfo(name = "favorite")
    var isFavorite: Boolean
) {
    override fun equals(other: Any?): Boolean {
        return this.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}


data class SortingValue(

    @ColumnInfo(name = "bestMatch")
    val bestMatch: Float,

    @ColumnInfo(name = "newest")
    val newest: Float,
    @ColumnInfo(name = "ratingAverage")
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