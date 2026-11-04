package com.fxrates.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** `GET /v1/latest` — the current working day's rates keyed by currency code. */
@Serializable
data class LatestRatesDto(
    @SerialName("base") val base: String,
    @SerialName("date") val date: String,
    @SerialName("rates") val rates: Map<String, Double>,
)
