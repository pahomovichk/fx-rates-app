package com.fxrates.feature.rates.details.model

import androidx.compose.runtime.Immutable

/**
 * The change between the oldest and the newest quote of the window.
 * Both numbers are painted from [trend].
 *
 * @property label already formatted, and it names both dates of the window.
 */
@Immutable
data class RateChangeItem(
    val label: String,
    val absolute: String,
    val percent: String,
    val trend: Trend,
)
