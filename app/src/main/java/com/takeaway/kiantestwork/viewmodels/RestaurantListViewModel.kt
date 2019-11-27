package com.takeaway.kiantestwork.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.takeaway.kiantestwork.dto.Restaurant
import com.takeaway.kiantestwork.dto.SortType
import com.takeaway.kiantestwork.repository.RestaurantRepository
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
    var sortType: SortType = SortType.DEFAULT_STATUS
        set(value) = loadRestaurants(value)

    private fun loadRestaurants(type: SortType) {
        loadingVisibility.value = View.VISIBLE

        repository.getRestaurantListDefaultSorting(type)
            .subscribeOn(Schedulers.io())
            .delay(3,TimeUnit.SECONDS)
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
}