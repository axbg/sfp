package main.kotlin.enums

enum class RefreshRateEnum(val refreshRate: Long) {
    ONE_SECOND(1000),
    FIVE_SECONDS(5000),
    TEN_SECONDS(10000),
    THIRTY_SECONDS(30000),
    ONE_MINUTE(60000),
    FIVE_MINUTES(300000),
    TEN_MINUTES(600000),
    THIRTY_MINUTES(1800000)
}