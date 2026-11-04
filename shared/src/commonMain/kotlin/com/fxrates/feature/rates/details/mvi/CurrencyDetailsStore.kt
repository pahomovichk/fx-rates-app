package com.fxrates.feature.rates.details.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.fxrates.core.utils.Resource
import com.fxrates.domain.model.RateQuote

internal class CurrencyDetailsStore(
    store: Store<Intent, State, Label>,
) : Store<CurrencyDetailsStore.Intent, CurrencyDetailsStore.State, CurrencyDetailsStore.Label> by store {

    sealed class Intent {

        data object OnRefresh : Intent()

        data object OnBack : Intent()

    }

    sealed class Action {

        data class LoadHistory(val base: String, val code: String) : Action()

    }

    sealed class Message {

        data class UpdateQuotes(val quotes: Resource<List<RateQuote>>) : Message()

    }

    sealed class Label {

        data object NavigateBack : Label()

    }

    data class State(
        val base: String,
        val code: String,
        val name: String,
        val quotes: Resource<List<RateQuote>> = Resource.Loading,
    )

}
