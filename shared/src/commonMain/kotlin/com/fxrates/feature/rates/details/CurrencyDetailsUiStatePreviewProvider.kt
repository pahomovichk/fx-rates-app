package com.fxrates.feature.rates.details

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.fxrates.feature.rates.details.CurrencyDetailsUiState.Content
import com.fxrates.feature.rates.details.model.HistoryItem
import com.fxrates.feature.rates.details.model.RateChangeItem
import com.fxrates.feature.rates.details.model.Trend

internal class CurrencyDetailsUiStatePreviewProvider : PreviewParameterProvider<CurrencyDetailsUiState> {

    override val values: Sequence<CurrencyDetailsUiState> = sequenceOf(
        CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            content = Content.Loading,
        ),
        CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            isRefreshEnabled = true,
            content = Content.Error,
        ),
        CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            isRefreshEnabled = true,
            content = Content.Empty,
        ),
        CurrencyDetailsUiState(
            code = "EUR",
            name = "Euro",
            isRefreshEnabled = true,
            content = Content.Data(
                rateLabel = "1 USD · 2026-11-04",
                rate = "0.8500",
                change = RateChangeItem(
                    label = "Change · 2026-10-05 – 2026-11-04",
                    absolute = "-0.0500",
                    percent = "-5.88%",
                    trend = Trend.Down,
                ),
                history = listOf(
                    HistoryItem(date = "2026-11-04", rate = "0.8500"),
                    HistoryItem(date = "2026-10-20", rate = "0.8800"),
                    HistoryItem(date = "2026-10-05", rate = "0.9000"),
                ),
            ),
        ),
        CurrencyDetailsUiState(
            code = "JPY",
            name = "Japanese Yen",
            isRefreshEnabled = true,
            content = Content.Data(
                rateLabel = "1 USD · 2026-11-04",
                rate = "157.9000",
                change = null,
                history = listOf(HistoryItem(date = "2026-11-04", rate = "157.9000")),
            ),
        ),
    )

}
