@file:Suppress("DEPRECATION")

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
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test


/**
 * usually we would mock the whole NetworkService's result
 * I might implement it another way in a real app but i think
 * this is well-balanced for the scope of this project.
 * tried to make the tests as generic as possible.
 * in order to make less changes if the dataset needs to be changed
 */
class RestaurantRepositoryTest : BaseTest() {
    private lateinit var repository: RestaurantRepository
    private lateinit var dataSource: LocalDataSource
    private lateinit var restaurants: List<Restaurant>
    private val gson = GsonBuilder().create()

    private fun initRestaurantList() {
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
            on { createRestaurant(restaurants[2]) } doAnswer {
                restaurants[2].isFavorite = true
                Single.just(1)
            }
            on { deleteRestaurant(restaurants[2]) } doAnswer {
                restaurants[2].isFavorite = false
                Single.just(1)
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
    fun `last item of list has to be a closed restaurant`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DEFAULT_STATUS).blockingGet()
        assertEquals(sortedList.last().status, "closed")
    }


    /**
     *
     * going to check 3 concrete scenarios per sorting value :
     * 1. favorites on top
     * 2. open -> order ahead -> closed
     * 3. each section should be sorted by the sorting value
     * (in scenario 3.we exclude favorites because there are not much favorite items in order to thest the favorites
     *  but they are in order and we should pass another dataset to test them as well. ther have no difference in coding)
     */


    @Test
    fun `check adding item to favorites works`() {
        repository.addRestaurantToFavorites(restaurants[2])
        assertEquals(4, restaurants.filter { it.isFavorite }.size)
    }

    @Test
    fun `check removing item from favorites works`() {
        repository.removeRestaurantFromFavorites(restaurants[2])
        assertEquals(3, restaurants.filter { it.isFavorite }.size)
    }

    @Test
    fun `Sort By Minimum Cost favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.MINIMUM_COST).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Minimum Cost puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.MINIMUM_COST).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Minimum Cost Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.MINIMUM_COST).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedAscendingProperly { it.sortingValues.minCost }

    }


    @Test
    fun `Sort By Average Price favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.AVERAGE_PRICE).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Average Price puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.AVERAGE_PRICE).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Average Price Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.AVERAGE_PRICE).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedAscendingProperly { it.sortingValues.averageProductPrice }

    }

    @Test
    fun `Sort By Delivery Cost favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DELIVERY_COST).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Delivery Cost puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DELIVERY_COST).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Delivery Cost Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DELIVERY_COST).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedAscendingProperly { it.sortingValues.deliveryCosts }

    }


    @Test
    fun `Sort By Popularity puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.POPULARITY).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Popularity puts favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.POPULARITY).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Popularity Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.POPULARITY).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedDescendingProperly { it.sortingValues.popularity }

    }


    @Test
    fun `Sort By Best Match favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.BEST_MATCH).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Best Match puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.BEST_MATCH).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Best Match Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.BEST_MATCH).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedDescendingProperly { it.sortingValues.bestMatch }

    }


    @Test
    fun `Sort By Distance favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DISTANCE).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Distance puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DISTANCE).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Distance Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.DISTANCE).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedAscendingProperly { it.sortingValues.distance }

    }

    @Test
    fun `Sort By Average Rate favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.AVERAGE_RATING).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Average Rate puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.AVERAGE_RATING).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Average Rate Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.AVERAGE_RATING).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedDescendingProperly { it.sortingValues.ratingAverage }

    }

    @Test
    fun `Sort By Newest favorites are on top`() {
        val sortedList = repository.getSortedRestaurantList(SortType.NEWEST).blockingGet()

        checkFavoritesOnTop(sortedList)

    }

    @Test
    fun `Sort By Newest puts restaurants in right Availability Order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.NEWEST).blockingGet()
        val favorites = sortedList.filter { it.isFavorite }
        val nonFavorites = sortedList.filter { !it.isFavorite }

        //this part of code may break if you change the data set to another one with no or less favorites
        checkOrderInAvailability(favorites)

        checkOrderInAvailability(nonFavorites)
    }

    @Test
    fun `Sort By Newest Puts items in right order`() {
        val sortedList = repository.getSortedRestaurantList(SortType.NEWEST).blockingGet()

        sortedList.filter { !it.isFavorite }
            .checkIfListSortedDescendingProperly { it.sortingValues.newest }

    }


    @Test
    fun `Check Searching pizza should return just one item`() {
        //going to check
        val sortedList =
            repository.filterRestaurantByName(SortType.DEFAULT_STATUS, "Pizza").blockingGet()
        assertEquals(1, sortedList.size)
    }


    @Test
    fun `Check Searching sushi should return 4 items`() {
        val sortedList =
            repository.filterRestaurantByName(SortType.DEFAULT_STATUS, "Sushi").blockingGet()
        assertEquals(4, sortedList.size)
    }


}