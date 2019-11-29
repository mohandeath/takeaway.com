package com.takeaway.kiantestwork.data.dto

enum class SortType(val key: String) {
    DEFAULT_STATUS("Availability"),
    NEWEST("Newest"),
    BEST_MATCH("Best Match"),
    DISTANCE("Nearest"),
    POPULARITY("Popularity"),
    AVERAGE_RATING("Average Rate"),
    AVERAGE_PRICE("Lowest Average Price"),
    MINIMUM_COST("Minimum Cost"),
    DELIVERY_COST("Delivery Cost"),
}