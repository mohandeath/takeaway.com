package com.takeaway.kiantestwork.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.*
import com.takeaway.kiantestwork.BaseTest
import com.takeaway.kiantestwork.RESPONSE_MOCK
import com.takeaway.kiantestwork.data.datasources.local.LocalDataSource
import com.takeaway.kiantestwork.data.dto.Restaurant
import com.takeaway.kiantestwork.data.dto.SortType
import com.takeaway.kiantestwork.data.repository.RestaurantRepository
import com.takeaway.kiantestwork.maxByShowLast
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test


/**
 * usually we would mock the whole NetworkService's result
 * I might implement it another way in a real app but i think
 * this is well-balanced for the scope of this project
 */
class RestaurantRepositoryTest : BaseTest() {
    private lateinit var repository: RestaurantRepository
    private lateinit var dataSource: LocalDataSource
    private lateinit var restaurants: List<Restaurant>
    private val gson = GsonBuilder().create()

    fun initRestaurantList() {
        val restaurantListType = object : TypeToken<ArrayList<Restaurant>>() {}.type
        restaurants = gson.fromJson(RESPONSE_MOCK, restaurantListType)
        //making two restaurants favorite by default
        restaurants[4].isFavorite = true // sample for opened restaurant
        restaurants[8].isFavorite = true // sample for order ahead restaurant
        restaurants[1].isFavorite = true // sample for closed restaurant


    }

    @Before
    fun init() {
        initRestaurantList()

        dataSource = mock {
            on { getRestaurantList() } doReturn restaurants
            on { createRestaurant(any()) } doAnswer {
                restaurants[2].isFavorite = true
                Single.just(any())
            }
            on { deleteRestaurant(any()) } doAnswer {
                restaurants[2].isFavorite = false
                Single.just(any())
            }
            on { getFavoriteRestaurants() } doReturn Single.just(restaurants.filter { it.isFavorite })

        }

        repository = RestaurantRepository(dataSource)
    }

    @Test
    fun `getSortedRestaurant calls the dataSource two times`() {
        repository.getSortedRestaurantList(SortType.AVERAGE_PRICE)
        verify(dataSource, times(1)).getRestaurantList()
        verify(dataSource, times(1)).getFavoriteRestaurants()

    }

    @Test
    fun `get sorted restaurant returns the favorites on top of the list`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DEFAULT_STATUS).blockingGet()
        assertEquals(sortedList.first().isFavorite, true)
    }

    @Test
    fun `last item of list have to be a closed restauratn`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DEFAULT_STATUS).blockingGet()
        assertEquals(sortedList.first().status, "closed")
    }


    /**
     *
     * going to check 3 concrete scenarios per sorting value :
     * 1. favorites on top
     * 2. open -> order ahead -> closed
     * 3. each section should be sorted by the sorting value
     */
    private fun checkFavoritesOnTop(list: List<Restaurant>) {
        val firstFavorideIndex = list.indexOfFirst { it.isFavorite }
        val firstNormalItemIndex = list.indexOfLast { !it.isFavorite }
        assert(firstFavorideIndex < firstNormalItemIndex)
    }


    private fun checkOrderInAvailability(list: List<Restaurant>) {
        val lastOpenIndex = list.indexOfLast { it.status == "open" }
        val firstOrderAheadIndex = list.indexOfFirst { it.status == "order ahead" }
        assert(lastOpenIndex < firstOrderAheadIndex)

        val lastOrderAheadIndex = list.indexOfLast { it.status == "order ahead" }
        val firstClosedIndex = list.indexOfFirst { it.status == "closed" }
        assert(lastOrderAheadIndex < firstClosedIndex)
    }

    @Test
    fun `Test Sort By Minimum Cost`() {
        val sortedList = repository.getSortedRestaurantList(SortType.MINIMUM_COST).blockingGet()
        checkFavoritesOnTop(sortedList)
        // checking availability for the favorites and also the rest of the lits
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the dataset to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)

        //minimum value of the sortType should be the first element in the list and max value at the last
        // we do it per each status type cause it should be sorted per each one of them

        val nonFavoriteOpen = nonFavorites.filter { it.status == "open" }
        assertEquals(0,
            nonFavoriteOpen.indexOf(
                nonFavoriteOpen.minBy { it.sortingValues.minCost })
        )
        assertEquals( nonFavoriteOpen.lastIndex,
            nonFavoriteOpen.indexOf(
                    nonFavoriteOpen.maxByShowLast { it.sortingValues.minCost })

        )


        val nonFavoriteOrderAhead = nonFavorites.filter { it.status == "order ahead" }
        assertEquals(0,
            nonFavoriteOrderAhead.indexOf(
                nonFavoriteOrderAhead.minBy { it.sortingValues.minCost })
        )

        assertEquals( nonFavoriteOrderAhead.lastIndex,
            nonFavoriteOrderAhead.indexOf(
                nonFavoriteOrderAhead.maxByShowLast { it.sortingValues.minCost })

        )

        val nonFavoriteClosed = nonFavorites.filter { it.status == "closed" }
        assertEquals(0,
            nonFavoriteClosed.indexOf(
                nonFavoriteClosed.minBy { it.sortingValues.minCost })
        )

        assertEquals( nonFavoriteClosed.lastIndex,
            nonFavoriteClosed.indexOf(
                nonFavoriteClosed.maxByShowLast { it.sortingValues.minCost })
            )







    }


}