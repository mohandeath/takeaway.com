package com.takeaway.kiantestwork.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.takeaway.kiantestwork.data.dto.Restaurant
import com.takeaway.kiantestwork.data.dto.SortType
import com.takeaway.kiantestwork.data.repository.RestaurantRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RestaurantListViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : BaseViewModel() {
    val restaurantList = MutableLiveData<List<Restaurant>>().apply { value = ArrayList() }
    val loadingVisibility = MutableLiveData<Int>().apply { value = View.GONE }
    val retryVisibility = MutableLiveData<Int>().apply { value = View.GONE }
    val errorMessage = MutableLiveData<String>()
    val infoMessage = MutableLiveData<String>()
    val sorting = listOf(
        SortType.DEFAULT_STATUS,
        SortType.BEST_MATCH,
        SortType.POPULARITY,
        SortType.NEWEST,
        SortType.DISTANCE,
        SortType.AVERAGE_RATING,
        SortType.AVERAGE_PRICE,
        SortType.MINIMUM_COST,
        SortType.DELIVERY_COST
    )

    var sortType: SortType = SortType.DEFAULT_STATUS
        set(value) {
            field = value
            loadRestaurants(value, query)
        }

    var query: String = ""
        set(value) {
            field = value
            loadRestaurants(sortType, value)
        }


    /**
     *
     * @return list of restaurants sorted by `type` and filterd by `query`
     * please notice that it optimises the results in a way that if the query
     * is empty, it don't bother filtering the results
     * used this method to improve the performance. it will be more helpful
     * if we had a NetworkDataSource and cuts off loads from the server and the client in this case
     */
    private fun loadRestaurants(type: SortType, query: String) {
        loadingVisibility.value = View.VISIBLE


        val result =
            if (query.isEmpty())
                repository.getSortedRestaurantList(type)
            else repository.filterRestaurantByName(type, query)

        result
            .subscribeOn(Schedulers.io())
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { restaurants ->
                    restaurantList.value = restaurants
                    loadingVisibility.value = View.GONE
                    retryVisibility.value = View.GONE
                }, { error ->
                    error.printStackTrace()
                    loadingVisibility.value = View.GONE
                    retryVisibility.value = View.VISIBLE
                    errorMessage.value = error.message

                })
            .also { addToUnsubscribe(it) }

    }

    fun addRestaurantToFavorites(restaurant: Restaurant) {
        repository.addRestaurantToFavorites(restaurant).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadRestaurants(sortType, query)
                infoMessage.value = " ${restaurant.name} has been added from favorites!"

            }, {
                it.printStackTrace()
                errorMessage.value = it.message


            }).also { addToUnsubscribe(it) }
    }

    fun removeRestaurantFromFavorites(restaurant: Restaurant) {
        repository.removeRestaurantFromFavorites(restaurant).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadRestaurants(sortType, query)
                infoMessage.value = " ${restaurant.name} has been removed from favorites!"

            }, {
                it.printStackTrace()
                errorMessage.value = it.message

            }).also { addToUnsubscribe(it) }
    }

    fun getCurrentSortIndex() = sorting.indexOf(sortType)
}