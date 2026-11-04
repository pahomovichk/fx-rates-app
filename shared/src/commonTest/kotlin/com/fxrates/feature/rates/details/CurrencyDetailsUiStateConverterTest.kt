package com.fxrates.feature.rates.details

import com.fxrates.core.utils.Resource
import com.fxrates.domain.model.RateQuote
import com.fxrates.feature.rates.details.CurrencyDetailsUiState.Content
import com.fxrates.feature.rates.details.model.HistoryItem
import com.fxrates.feature.rates.details.model.RateChangeItem
import com.fxrates.feature.rates.details.model.Trend
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore
import com.fxrates.testing.rateQuote
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class CurrencyDetailsUiStateConverterTest : FunSpec({

    val converter = CurrencyDetailsUiStateConverter()

    fun state(
        quotes: Resource<List<RateQuote>> = Resource.Loading,
        code: String = "EUR",
        name: String = "Euro",
        base: String = "USD",
    ): CurrencyDetailsStore.State = CurrencyDetailsStore.State(
        base = base,
        code = code,
        name = name,
        quotes = quotes,
    )

    test("several quotes show the latest rate, the reversed history and a signed change") {
        val quotes = listOf(
            rateQuote(date = LocalDate(2026, 10, 5), rate = "2"),
            rateQuote(date = LocalDate(2026, 10, 20), rate = "1.5"),
            rateQuote(date = LocalDate(2026, 11, 4), rate = "1"),
        )

        val uiState = converter.createUiState(state(quotes = Resource.Data(quotes)))

        uiState shouldBe CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            isRefreshEnabled = true,
            content = Content.Data(
                rateLabel = "1 USD · 2026-11-04",
                rate = "1.0000",
                change = RateChangeItem(
                    label = "Change · 2026-10-05 – 2026-11-04",
                    absolute = "-1.0000",
                    percent = "-100.00%",
                    trend = Trend.Down,
                ),
                history = listOf(
                    HistoryItem(date = "2026-11-04", rate = "1.0000"),
                    HistoryItem(date = "2026-10-20", rate = "1.5000"),
                    HistoryItem(date = "2026-10-05", rate = "2.0000"),
                ),
            ),
        )
    }

    test("a rising rate shows a positive change") {
        val quotes = listOf(
            rateQuote(date = LocalDate(2026, 10, 5), rate = "1"),
            rateQuote(date = LocalDate(2026, 11, 4), rate = "2"),
        )

        val content = converter.createUiState(state(quotes = Resource.Data(quotes))).content

        content shouldBe Content.Data(
            rateLabel = "1 USD · 2026-11-04",
            rate = "2.0000",
            change = RateChangeItem(
                label = "Change · 2026-10-05 – 2026-11-04",
                absolute = "+1.0000",
                percent = "+50.00%",
                trend = Trend.Up,
            ),
            history = listOf(
                HistoryItem(date = "2026-11-04", rate = "2.0000"),
                HistoryItem(date = "2026-10-05", rate = "1.0000"),
            ),
        )
    }

    test("a single quote has a rate and no change") {
        val quote = rateQuote(date = LocalDate(2026, 11, 4), rate = "157.9")

        val uiState = converter.createUiState(state(quotes = Resource.Data(listOf(quote))))

        uiState.content shouldBe Content.Data(
            rateLabel = "1 USD · 2026-11-04",
            rate = "157.9000",
            change = null,
            history = listOf(HistoryItem(date = "2026-11-04", rate = "157.9000")),
        )
    }

    test("no quotes give the empty state") {
        val uiState = converter.createUiState(state(quotes = Resource.Data(emptyList())))

        uiState shouldBe CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            isRefreshEnabled = true,
            content = Content.Empty,
        )
    }

    test("a zero earliest quote leaves the rate and the history without a change") {
        val quotes = listOf(
            rateQuote(date = LocalDate(2026, 10, 5), rate = "0"),
            rateQuote(date = LocalDate(2026, 11, 4), rate = "1.5"),
        )

        val content = converter.createUiState(state(quotes = Resource.Data(quotes))).content

        content shouldBe Content.Data(
            rateLabel = "1 USD · 2026-11-04",
            rate = "1.5000",
            change = null,
            history = listOf(
                HistoryItem(date = "2026-11-04", rate = "1.5000"),
                HistoryItem(date = "2026-10-05", rate = "0.0000"),
            ),
        )
    }

    test("a zero difference keeps an unsigned flat change") {
        val quotes = listOf(
            rateQuote(date = LocalDate(2026, 10, 5), rate = "0.9"),
            rateQuote(date = LocalDate(2026, 11, 4), rate = "0.9"),
        )

        val content = converter.createUiState(state(quotes = Resource.Data(quotes))).content

        content shouldBe Content.Data(
            rateLabel = "1 USD · 2026-11-04",
            rate = "0.9000",
            change = RateChangeItem(
                label = "Change · 2026-10-05 – 2026-11-04",
                absolute = "0.0000",
                percent = "0.00%",
                trend = Trend.Flat,
            ),
            history = listOf(
                HistoryItem(date = "2026-11-04", rate = "0.9000"),
                HistoryItem(date = "2026-10-05", rate = "0.9000"),
            ),
        )
    }

    test("a difference that rounds to zero keeps an unsigned flat change") {
        val quotes = listOf(
            rateQuote(date = LocalDate(2026, 10, 5), rate = "100"),
            rateQuote(date = LocalDate(2026, 11, 4), rate = "100.00004"),
        )

        val content = converter.createUiState(state(quotes = Resource.Data(quotes))).content

        content shouldBe Content.Data(
            rateLabel = "1 USD · 2026-11-04",
            rate = "100.0000",
            change = RateChangeItem(
                label = "Change · 2026-10-05 – 2026-11-04",
                absolute = "0.0000",
                percent = "0.00%",
                trend = Trend.Flat,
            ),
            history = listOf(
                HistoryItem(date = "2026-11-04", rate = "100.0000"),
                HistoryItem(date = "2026-10-05", rate = "100.0000"),
            ),
        )
    }

    test("loading shows the code and the name and disables refresh") {
        val uiState = converter.createUiState(state(code = "EUR", name = "Euro"))

        uiState shouldBe CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            isRefreshEnabled = false,
            content = Content.Loading,
        )
    }

    test("an error shows the code and the name and keeps refresh enabled") {
        val uiState = converter.createUiState(
            state(
                quotes = Resource.Error(IllegalStateException("offline")),
                code = "JPY",
                name = "Japanese Yen",
            ),
        )

        uiState shouldBe CurrencyDetailsUiState(
            code = "JPY",
            name = "Japanese Yen",
            isRefreshEnabled = true,
            content = Content.Error,
        )
    }

})
