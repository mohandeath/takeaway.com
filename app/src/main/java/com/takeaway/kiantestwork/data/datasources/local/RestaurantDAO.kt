package com.takeaway.kiantestwork.data.datasources.local

import androidx.room.Dao
import androidx.room.Query
import com.takeaway.kiantestwork.data.dto.Restaurant
import io.reactivex.Single

@Dao
interface RestaurantDAO {
    @Query("SELECT * FROM restaurant")
    fun getFavoritedRestaurants(): Single<List<Restaurant>>

}
