package com.fxrates.data.repository

import com.fxrates.data.remote.dto.LatestRatesDto
import com.fxrates.data.remote.dto.TimeSeriesDto
import com.fxrates.domain.model.CurrencyRate
import com.fxrates.domain.model.RateQuote
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class RatesConverterTest : FunSpec({

    val converter = RatesConverter()

    test("the last date is the latest rate and the one before it is the previous rate") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-10-29",
            endDate = "2026-11-02",
            rates = mapOf(
                "2026-11-02" to mapOf("EUR" to 0.89158),
                "2026-10-29" to mapOf("EUR" to 0.89847),
                "2026-10-30" to mapOf("EUR" to 0.89509),
            ),
        )

        val rates = converter.toLatestRates(timeSeries, currencies = mapOf("EUR" to "Euro"))

        rates shouldHaveSize 1
        with(rates.single()) {
            code shouldBe "EUR"
            name shouldBe "Euro"
            date shouldBe LocalDate(2026, 11, 2)
            rate shouldBe "0.89158".toBigDecimal()
            previousRate shouldBe "0.89509".toBigDecimal()
        }
    }

    test("a single date gives no previous rate and an unknown code keeps its code as the name") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-11-02",
            endDate = "2026-11-02",
            rates = mapOf("2026-11-02" to mapOf("XAU" to 0.0003)),
        )

        val rate = converter.toLatestRates(timeSeries, currencies = emptyMap()).single()

        rate.name shouldBe "XAU"
        rate.previousRate shouldBe null
    }

    test("the rates are sorted by code") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-11-02",
            endDate = "2026-11-02",
            rates = mapOf("2026-11-02" to mapOf("JPY" to 157.9, "CHF" to 0.8, "EUR" to 0.89)),
        )

        val codes = converter.toLatestRates(timeSeries, currencies = emptyMap()).map(CurrencyRate::code)

        codes shouldBe listOf("CHF", "EUR", "JPY")
    }

    test("an empty time series gives no rates") {
        val timeSeries = TimeSeriesDto(base = "USD", startDate = "", endDate = "", rates = emptyMap())

        converter.toLatestRates(timeSeries, currencies = emptyMap()).shouldBeEmpty()
    }

    test("the current working day keeps the decimal digits and has no previous rate") {
        val latest = LatestRatesDto(
            base = "USD",
            date = "2026-11-03",
            rates = mapOf("EUR" to 0.89158, "XAU" to 0.0003),
        )

        val rates = converter.toCurrentRates(latest, currencies = mapOf("EUR" to "Euro"))

        rates shouldHaveSize 2
        with(rates.first { rate -> rate.code == "EUR" }) {
            name shouldBe "Euro"
            date shouldBe LocalDate(2026, 11, 3)
            rate shouldBe "0.89158".toBigDecimal()
            previousRate shouldBe null
        }
        with(rates.first { rate -> rate.code == "XAU" }) {
            name shouldBe "XAU"
            previousRate shouldBe null
        }
    }

    test("the current working day's rates are sorted by code") {
        val latest = LatestRatesDto(
            base = "USD",
            date = "2026-11-03",
            rates = mapOf("JPY" to 157.9, "CHF" to 0.8, "EUR" to 0.89),
        )

        val codes = converter.toCurrentRates(latest, currencies = emptyMap()).map(CurrencyRate::code)

        codes shouldBe listOf("CHF", "EUR", "JPY")
    }

    test("only the requested currency is kept from a series of every currency") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-10-29",
            endDate = "2026-11-02",
            rates = mapOf(
                "2026-11-02" to mapOf("EUR" to 0.89158, "JPY" to 157.9),
                "2026-10-29" to mapOf("EUR" to 0.89847, "JPY" to 156.1),
            ),
        )

        val quotes = converter.toQuotes(timeSeries, code = "EUR")

        quotes shouldHaveSize 2
        quotes.map(RateQuote::rate) shouldBe listOf("0.89847".toBigDecimal(), "0.89158".toBigDecimal())
    }

    test("a day without the requested currency is skipped") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-10-29",
            endDate = "2026-11-02",
            rates = mapOf(
                "2026-10-29" to mapOf("EUR" to 0.89847),
                "2026-10-30" to mapOf("JPY" to 157.9),
                "2026-11-02" to mapOf("EUR" to 0.89158),
            ),
        )

        val quotes = converter.toQuotes(timeSeries, code = "EUR")

        quotes.map(RateQuote::date) shouldBe listOf(LocalDate(2026, 10, 29), LocalDate(2026, 11, 2))
    }

    test("an empty time series gives no quotes") {
        val timeSeries = TimeSeriesDto(base = "USD", startDate = "", endDate = "", rates = emptyMap())

        converter.toQuotes(timeSeries, code = "EUR").shouldBeEmpty()
    }

    test("a series without the requested currency gives no quotes") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-11-02",
            endDate = "2026-11-02",
            rates = mapOf("2026-11-02" to mapOf("JPY" to 157.9)),
        )

        converter.toQuotes(timeSeries, code = "EUR").shouldBeEmpty()
    }

    test("quotes are sorted by date even when the input map is not") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-10-29",
            endDate = "2026-11-02",
            rates = mapOf(
                "2026-11-02" to mapOf("EUR" to 0.89158),
                "2026-10-29" to mapOf("EUR" to 0.89847),
                "2026-10-30" to mapOf("EUR" to 0.89509),
            ),
        )

        val dates = converter.toQuotes(timeSeries, code = "EUR").map(RateQuote::date)

        dates shouldBe listOf(LocalDate(2026, 10, 29), LocalDate(2026, 10, 30), LocalDate(2026, 11, 2))
    }

    test("the decimal digits of a quote are kept") {
        val timeSeries = TimeSeriesDto(
            base = "USD",
            startDate = "2026-11-02",
            endDate = "2026-11-02",
            rates = mapOf("2026-11-02" to mapOf("EUR" to 0.89158)),
        )

        converter.toQuotes(timeSeries, code = "EUR").single().rate shouldBe "0.89158".toBigDecimal()
    }

})
