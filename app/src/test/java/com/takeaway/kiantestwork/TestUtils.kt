package com.takeaway.kiantestwork


/**
 * Returns the first element yielding the largest value of the given function or `null` if there are no elements.
 *
 * @sample samples.collections.Collections.Aggregates.maxBy
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