package com.fxrates.feature.rates.details.mvi

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.fxrates.domain.repository.RatesRepository
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.Action
import com.fxrates.feature.rates.details.mvi.CurrencyDetailsStore.State
import me.tatarka.inject.annotations.Inject

@Inject
internal class CurrencyDetailsStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RatesRepository,
) {

    fun createStore(base: String, code: String, name: String): CurrencyDetailsStore {
        val store = storeFactory.create(
            name = "CurrencyDetailsStore",
            initialState = State(base = base, code = code, name = name),
            bootstrapper = SimpleBootstrapper(Action.LoadHistory(base = base, code = code)),
            executorFactory = { CurrencyDetailsExecutor(repository = repository) },
            reducer = { message -> reduce(message) },
        )
        return CurrencyDetailsStore(store)
    }

}
