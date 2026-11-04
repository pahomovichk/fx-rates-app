package com.fxrates.feature.rates.details.mvi

import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.Message
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.State

internal fun State.reduce(message: Message): State = when (message) {
    is Message.UpdateQuotes -> copy(quotes = message.quotes)
}
