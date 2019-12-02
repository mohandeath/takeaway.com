@file:Suppress("DEPRECATION")

package com.takeaway.kiantestwork.viewmodel

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.takeaway.kiantestwork.BaseTest
import com.takeaway.kiantestwork.data.dto.SortType
import com.takeaway.kiantestwork.data.repository.RestaurantRepository
import com.takeaway.kiantestwork.viewmodels.RestaurantListViewModel
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantListViewModelTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var repository: RestaurantRepository
    lateinit var viewModel: RestaurantListViewModel

    @Before
    fun init() {

        initRestaurantList()

        repository = mock {
            on { getSortedRestaurantList(any()) } doReturn Single.just(restaurants)
            on { filterRestaurantByName(any(), any()) } doReturn Single.just(arrayListOf())
            on { addRestaurantToFavorites(any()) } doReturn Single.just(1L)
            on { removeRestaurantFromFavorites(any()) } doReturn Single.just(1)
        }

        viewModel = RestaurantListViewModel(repository)
    }


    @Test
    fun `When init it does not call for restaurant list`() {
        verify(repository, times(0)).getSortedRestaurantList(any())

    }

    @Test
    fun `when init loading is now shown`() {
        assertEquals(View.GONE, viewModel.loadingVisibility.value)
    }


    @Test
    fun `After showing list loading is not shown`() {
        viewModel.sortType = SortType.DEFAULT_STATUS
        assertEquals(View.GONE, viewModel.loadingVisibility.value)
    }

    @Test
    fun `Setting sortType leads to repository call `() {
        viewModel.sortType = SortType.DEFAULT_STATUS
        verify(repository, times(1)).getSortedRestaurantList(SortType.DEFAULT_STATUS)
    }

    @Test
    fun `Setting query search leads to repository call for filter `() {
        viewModel.query = "pizza"
        verify(repository, times(1)).filterRestaurantByName(viewModel.sortType, "pizza")
    }

    @Test
    fun `adding item to favorites calls repo for add and then laods the list`() {
        viewModel.addRestaurantToFavorites(restaurants[0])
        verify(repository, times(1)).addRestaurantToFavorites(restaurants[0])
        verify(repository, times(1)).getSortedRestaurantList(viewModel.sortType)

    }

    @Test
    fun `removing item from favorites calls repo for add and then loads the list`() {
        viewModel.removeRestaurantFromFavorites(restaurants[0])
        verify(repository, times(1)).removeRestaurantFromFavorites(restaurants[0])
        verify(repository, times(1)).getSortedRestaurantList(viewModel.sortType)

    }
}