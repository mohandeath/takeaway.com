package com.takeaway.kiantestwork.dto

enum class SortItem(val key: String) {
    NEWEST("sort_by_newest"),
    BEST_MATCH("sort_by_best_match"),
    DISTANCE("sort_by_distance"),
    POPULARITY("sort_by_popularity"),
    AVERAGE_RATING("sort_by_average_rating"),
    AVERAGE_PRICE("sort_by_average_price"),
    MINIMUM_COST("sort_by_minimum_cost"),
    DELIVERY_COST("sort_by_delivery_cost"),
}