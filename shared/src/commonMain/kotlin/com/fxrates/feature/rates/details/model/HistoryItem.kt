package com.fxrates.feature.rates.details.model

import androidx.compose.runtime.Immutable

/** One history row. [date] and [rate] are already formatted for display. */
@Immutable
data class HistoryItem(
    val date: String,
    val rate: String,
)
