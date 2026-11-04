package com.fxrates.domain.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate

/**
 * One day's rate of a currency pair. The base and the quote code belong to the request,
 * not to this model.
 *
 * @property rate how many units of the quote currency one unit of the base currency buys.
 */
data class RateQuote(
    val date: LocalDate,
    val rate: BigDecimal,
)
