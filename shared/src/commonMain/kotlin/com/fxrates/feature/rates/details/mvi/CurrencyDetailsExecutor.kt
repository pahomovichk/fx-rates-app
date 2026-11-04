package com.fxrates.feature.rates.details.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.fxrates.core.utils.resourceFlow
import com.fxrates.domain.repository.RatesRepository
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.Action
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.Intent
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.Label
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.Message
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class CurrencyDetailsExecutor(
    private val repository: RatesRepository,
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) = when (action) {
        is Action.LoadHistory -> loadHistory(action)
    }

    override fun executeIntent(intent: Intent) = when (intent) {
        is Intent.OnRefresh -> forward(Action.LoadHistory(base = state().base, code = state().code))
        is Intent.OnBack -> publish(Label.NavigateBack)
    }

    private fun loadHistory(action: Action.LoadHistory) {
        resourceFlow { repository.getRateHistory(base = action.base, code = action.code) }
            .onEach { quotes -> dispatch(Message.UpdateQuotes(quotes)) }
            .launchIn(scope)
    }

}
