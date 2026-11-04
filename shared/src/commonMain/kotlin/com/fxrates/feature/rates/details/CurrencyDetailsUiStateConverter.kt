package com.fxrates.feature.rates.details

import com.fxrates.core.format.NumberFormatter
import com.fxrates.core.money.RateChangeCalculator
import com.fxrates.core.utils.Resource
import com.fxrates.domain.model.RateQuote
import com.fxrates.feature.rates.details.CurrencyDetailsUiState.Content
import com.fxrates.feature.rates.details.model.HistoryItem
import com.fxrates.feature.rates.details.model.RateChangeItem
import com.fxrates.feature.rates.details.model.Trend
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore
import com.fxrates.ui.Strings
import me.tatarka.inject.annotations.Inject

@Inject
internal class CurrencyDetailsUiStateConverter {

    fun createUiState(state: CurrencyDetailsStore.State): CurrencyDetailsUiState {
        return CurrencyDetailsUiState(
            code = state.code,
            name = state.name,
            isRefreshEnabled = state.quotes !is Resource.Loading,
            content = createContent(base = state.base, quotes = state.quotes),
        )
    }

    private fun createContent(base: String, quotes: Resource<List<RateQuote>>): Content = when (quotes) {
        is Resource.Loading -> {
            Content.Loading
        }
        is Resource.Error -> {
            Content.Error
        }
        is Resource.Data -> {
            if (quotes.value.isEmpty()) {
                Content.Empty
            } else {
                toData(base = base, quotes = quotes.value)
            }
        }
    }

    private fun toData(base: String, quotes: List<RateQuote>): Content.Data {
        val latest = quotes.last()
        return Content.Data(
            rateLabel = Strings.ratesSubtitle(base, latest.date.toString()),
            rate = NumberFormatter.formatRate(latest.rate),
            change = toChange(quotes),
            history = quotes.asReversed().map { quote ->
                HistoryItem(
                    date = quote.date.toString(),
                    rate = NumberFormatter.formatRate(quote.rate),
                )
            },
        )
    }

    private fun toChange(quotes: List<RateQuote>): RateChangeItem? {
        if (quotes.size < 2) {
            return null
        }
        val oldest = quotes.first()
        val newest = quotes.last()
        val change = RateChangeCalculator.calculate(previous = oldest.rate, current = newest.rate) ?: return null
        val absolute = NumberFormatter.formatSignedRate(change.absolute)
        return RateChangeItem(
            label = Strings.detailsChange(oldest = oldest.date.toString(), newest = newest.date.toString()),
            absolute = absolute,
            percent = NumberFormatter.formatSignedPercent(change.percent),
            trend = trend(absolute),
        )
    }

    private fun trend(formattedAbsolute: String): Trend = when {
        formattedAbsolute.startsWith("+") -> Trend.Up
        formattedAbsolute.startsWith("-") -> Trend.Down
        else -> Trend.Flat
    }

}
