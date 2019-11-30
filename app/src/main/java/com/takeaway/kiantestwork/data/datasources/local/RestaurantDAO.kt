package com.takeaway.kiantestwork.data.datasources.local

import androidx.room.*
import com.takeaway.kiantestwork.data.dto.Restaurant
import io.reactivex.Single

@Dao
interface RestaurantDAO {
    @Query("SELECT * FROM restaurant")
    fun getRestaurants(): Single<List<Restaurant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(restaurant: Restaurant):Single<Long>

    @Delete
    fun removeRestaurant(restaurant: Restaurant):Single<Int>
}
