package com.fxrates.ui

/**
 * Every user-facing string of the app. The app is English-only, so a constant is enough; a
 * string is never written inline in a composable.
 */
object Strings {

    const val RATES_TITLE = "Rates"
    const val RATES_SEARCH_HINT = "Search by code or name"
    const val RATES_NOTHING_FOUND = "Nothing found"
    const val ERROR_TITLE = "Could not load the rates"
    const val RETRY = "Retry"
    const val REFRESH = "Refresh"
    const val BACK = "Back"
    const val DETAILS_ERROR_TITLE = "Could not load the rate"
    const val DETAILS_EMPTY = "No rates for this period"

    fun ratesSubtitle(base: String, date: String): String = "1 $base · $date"

    fun detailsChange(oldest: String, newest: String): String = "Change · $oldest – $newest"

}
