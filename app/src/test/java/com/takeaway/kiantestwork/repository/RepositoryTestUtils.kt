@file:Suppress("DEPRECATION")

package com.takeaway.kiantestwork.repository

import com.takeaway.kiantestwork.data.dto.Restaurant
import junit.framework.Assert.assertEquals


fun <R : Comparable<R>> List<Restaurant>.checkIfListSortedDescendingProperly(selector: (Restaurant) -> R) {
    filter { it.status == "open" }.testSortDescending(selector)
    filter { it.status == "order ahead" }.testSortDescending(selector)
    filter { it.status == "closed" }.testSortDescending(selector)
}

fun <R : Comparable<R>> List<Restaurant>.checkIfListSortedAscendingProperly(selector: (Restaurant) -> R) {
    filter { it.status == "open" }.testSortAcsending(selector)
    filter { it.status == "order ahead" }.testSortAcsending(selector)
    filter { it.status == "closed" }.testSortAcsending(selector)
}

fun checkFavoritesOnTop(list: List<Restaurant>) {
    val firstFavoriteIndex = list.indexOfFirst { it.isFavorite }
    val firstNormalItemIndex = list.indexOfFirst { !it.isFavorite }
    assert(firstFavoriteIndex < firstNormalItemIndex)
}


fun checkOrderInAvailability(list: List<Restaurant>) {
    val lastOpenIndex = list.indexOfLast { it.status == "open" }
    val firstOrderAheadIndex = list.indexOfFirst { it.status == "order ahead" }
    assert(lastOpenIndex < firstOrderAheadIndex)

    val lastOrderAheadIndex = list.indexOfLast { it.status == "order ahead" }
    val firstClosedIndex = list.indexOfFirst { it.status == "closed" }
    assert(lastOrderAheadIndex < firstClosedIndex)
}

/**
 * Its like the original MaxBy with a little difference that
 * shows the last item with max value instead of the first one
 */
inline fun <T, R : Comparable<R>> Iterable<T>.maxByShowLast(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var maxElem = iterator.next()
    if (!iterator.hasNext()) return maxElem
    var maxValue = selector(maxElem)
    do {
        val e = iterator.next()
        val v = selector(e)
        if (maxValue <= v) {
            maxElem = e
            maxValue = v
        }
    } while (iterator.hasNext())
    return maxElem
}


inline fun <T, R : Comparable<R>> Iterable<T>.minByShowLast(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElem = iterator.next()
    if (!iterator.hasNext()) return minElem
    var minValue = selector(minElem)
    do {
        val e = iterator.next()
        val v = selector(e)
        if (minValue >= v) {
            minElem = e
            minValue = v
        }
    } while (iterator.hasNext())
    return minElem
}


/**
 * Checking if the restaurants are sorting in the requested Manner
 * first element should be the one with the HIGHEST value
 * and the last element should be the one with the HIGHEST value
 */
inline fun <T, R : Comparable<R>> List<T>.testSortAcsending(selector: (T) -> R) {

    assertEquals(0, indexOf(minBy(selector)))
    assertEquals(lastIndex, indexOf(maxByShowLast(selector)))
}

/**
 * Checking if the restaurants are sorting in the requested Manner
 * first element should be the one with the HIGHEST value
 * and the last element should be the one with the LOWEST value
 */
inline fun <T, R : Comparable<R>> List<T>.testSortDescending(selector: (T) -> R) {

    assertEquals(lastIndex, indexOf(minByShowLast(selector)))
    assertEquals(0, indexOf(maxBy(selector)))
}


