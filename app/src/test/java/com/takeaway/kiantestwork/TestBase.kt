package com.takeaway.kiantestwork


import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.takeaway.kiantestwork.data.dto.Restaurant
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.BeforeClass
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

open class BaseTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpBaseClass() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(
                    run: Runnable,
                    delay: Long,
                    unit: TimeUnit
                ): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): ExecutorScheduler.ExecutorWorker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

        }
    }

    protected lateinit var restaurants: List<Restaurant>
    private val gson = GsonBuilder().create()

    protected fun initRestaurantList() {
        val restaurantListType = object : TypeToken<ArrayList<Restaurant>>() {}.type
        restaurants = gson.fromJson(RESPONSE_MOCK, restaurantListType)
        //making two restaurants favorite by default
        restaurants[4].isFavorite = true // sample for opened restaurant
        restaurants[8].isFavorite = true // sample for order ahead restaurant
        restaurants[1].isFavorite = true // sample for closed restaurant


    }
}
