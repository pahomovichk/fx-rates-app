package com.fxrates.domain.repository

import com.fxrates.domain.model.CurrencyRate
import com.fxrates.domain.model.RateQuote

interface RatesRepository {

    /** The latest rates of every supported currency against [base], sorted by code. */
    suspend fun getLatestRates(base: String): List<CurrencyRate>

    /** The current working day's rates of every supported currency against [base], sorted by code. */
    suspend fun getCurrentRates(base: String): List<CurrencyRate>

    /** Daily quotes of [code] against [base] for the last thirty calendar days, oldest first. */
    suspend fun getRateHistory(base: String, code: String): List<RateQuote>

}
