package com.fxrates.feature.rates.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.fxrates.feature.rates.details.CurrencyDetailsUiState.Content
import com.fxrates.feature.rates.details.model.HistoryItem
import com.fxrates.feature.rates.details.model.RateChangeItem
import com.fxrates.feature.rates.details.model.Trend
import com.fxrates.ui.Strings
import com.fxrates.ui.theme.AppTheme

@Composable
fun CurrencyDetailsUiContent(
    component: CurrencyDetailsUiComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()
    CurrencyDetailsUiContent(
        state = state,
        onRefresh = component::onRefresh,
        onBack = component::onBack,
        modifier = modifier,
    )
}

@Composable
private fun CurrencyDetailsUiContent(
    state: CurrencyDetailsUiState,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .fillMaxSize()
        .safeDrawingPadding(),
) {
    Header(
        code = state.code,
        name = state.name,
        isRefreshEnabled = state.isRefreshEnabled,
        onBack = onBack,
        onRefresh = onRefresh,
    )
    when (val content = state.content) {
        is Content.Loading -> {
            Centered { CircularProgressIndicator() }
        }
        is Content.Error -> {
            ErrorState(onRetry = onRefresh)
        }
        is Content.Empty -> {
            Centered { Text(Strings.DETAILS_EMPTY, style = MaterialTheme.typography.bodyLarge) }
        }
        is Content.Data -> {
            DataState(content = content)
        }
    }
}

@Composable
private fun Header(
    code: String,
    name: String,
    isRefreshEnabled: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
) = Column(modifier = Modifier.fillMaxWidth()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = onBack) {
            Text(Strings.BACK)
        }
        TextButton(onClick = onRefresh, enabled = isRefreshEnabled) {
            Text(Strings.REFRESH)
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = AppTheme.spacing.m),
    ) {
        Text(code, style = MaterialTheme.typography.headlineMedium)
        Text(name, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.neutral)
    }
}

@Composable
private fun DataState(content: Content.Data) = LazyColumn(modifier = Modifier.fillMaxSize()) {
    item {
        CurrentRate(rate = content.rate, rateLabel = content.rateLabel)
        val change = content.change
        if (change != null) {
            RateChange(change = change)
        }
        HorizontalDivider()
    }
    items(items = content.history, key = HistoryItem::date) { item ->
        HistoryRow(item = item)
        HorizontalDivider()
    }
}

@Composable
private fun CurrentRate(
    rate: String,
    rateLabel: String,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = AppTheme.spacing.m, vertical = AppTheme.spacing.s),
) {
    Text(rate, style = MaterialTheme.typography.headlineMedium)
    Text(rateLabel, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.neutral)
}

@Composable
private fun RateChange(change: RateChangeItem) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = AppTheme.spacing.m, vertical = AppTheme.spacing.s),
) {
    Text(change.label, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.neutral)
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.m)) {
        Text(change.absolute, style = MaterialTheme.typography.titleMedium, color = change.trend.color())
        Text(change.percent, style = MaterialTheme.typography.titleMedium, color = change.trend.color())
    }
}

@Composable
private fun HistoryRow(item: HistoryItem) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = AppTheme.spacing.m, vertical = AppTheme.spacing.s),
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        item.date,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.weight(1f),
    )
    Text(item.rate, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun ErrorState(onRetry: () -> Unit) = Centered {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
    ) {
        Text(Strings.DETAILS_ERROR_TITLE, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry) {
            Text(Strings.RETRY)
        }
    }
}

@Composable
private fun Centered(content: @Composable () -> Unit) = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
) {
    content.invoke()
}

@Composable
private fun Trend.color() = when (this) {
    Trend.Up -> AppTheme.colors.positive
    Trend.Down -> AppTheme.colors.negative
    Trend.Flat -> AppTheme.colors.neutral
}

@Preview
@Composable
private fun CurrencyDetailsUiContentPreview(
    @PreviewParameter(CurrencyDetailsUiStatePreviewProvider::class) state: CurrencyDetailsUiState,
) = AppTheme {
    CurrencyDetailsUiContent(
        state = state,
        onRefresh = {},
        onBack = {},
    )
}
