package com.fxrates.feature.rates.details

import com.fxrates.core.utils.Empty
import com.fxrates.core.utils.Resource
import com.fxrates.domain.model.RateQuote
import com.fxrates.feature.rates.details.CurrencyDetailsUiState.Content
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore
import me.tatarka.inject.annotations.Inject

@Inject
internal class CurrencyDetailsUiStateConverter {

    fun createUiState(state: CurrencyDetailsStore.State): CurrencyDetailsUiState {
        return CurrencyDetailsUiState(
            code = state.code,
            name = state.name,
            isRefreshEnabled = state.quotes !is Resource.Loading,
            content = createContent(quotes = state.quotes),
        )
    }

    private fun createContent(quotes: Resource<List<RateQuote>>): Content = when (quotes) {
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
                Content.Data(
                    rateLabel = String.Empty,
                    rate = String.Empty,
                    change = null,
                    history = emptyList(),
                )
            }
        }
    }

}
