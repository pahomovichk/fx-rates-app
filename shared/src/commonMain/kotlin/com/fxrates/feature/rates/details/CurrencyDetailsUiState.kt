package com.fxrates.feature.rates.details

import androidx.compose.runtime.Immutable
import com.fxrates.core.utils.Empty
import com.fxrates.feature.rates.details.model.HistoryItem
import com.fxrates.feature.rates.details.model.RateChangeItem

@Immutable
internal data class CurrencyDetailsUiState(
    val code: String = String.Empty,
    val name: String = String.Empty,
    val isRefreshEnabled: Boolean = false,
    val content: Content = Content.Loading,
) {

    @Immutable
    sealed class Content {

        data object Loading : Content()

        data object Error : Content()

        data object Empty : Content()

        data class Data(
            val rateLabel: String = String.Empty,
            val rate: String = String.Empty,
            val change: RateChangeItem? = null,
            val history: List<HistoryItem> = emptyList(),
        ) : Content()

    }

}
