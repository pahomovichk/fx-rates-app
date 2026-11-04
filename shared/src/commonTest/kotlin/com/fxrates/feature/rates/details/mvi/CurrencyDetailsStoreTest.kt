package com.fxrates.feature.rates.details.mvi

import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.fxrates.core.utils.Resource
import com.fxrates.testing.FakeRatesRepository
import com.fxrates.testing.rateQuote
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyDetailsStoreTest : FunSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    beforeSpec {
        isAssertOnMainThreadEnabled = false
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
        isAssertOnMainThreadEnabled = true
    }

    fun createStore(repository: FakeRatesRepository): CurrencyDetailsStore {
        val factory = CurrencyDetailsStoreFactory(storeFactory = DefaultStoreFactory(), repository = repository)
        return factory.createStore(base = "USD", code = "EUR", name = "Euro")
    }

    test("the history is loaded for the base and code on start and the name is kept") {
        val quotes = listOf(
            rateQuote(date = LocalDate(2026, 10, 7), rate = "0.89"),
            rateQuote(date = LocalDate(2026, 11, 4), rate = "0.91"),
        )
        val repository = FakeRatesRepository().apply { rateHistory = Result.success(quotes) }

        val store = createStore(repository)

        repository.requestedHistory shouldBe listOf("USD" to "EUR")
        store.state shouldBe CurrencyDetailsStore.State(
            base = "USD",
            code = "EUR",
            name = "Euro",
            quotes = Resource.Data(quotes),
        )
    }

    test("a failed load ends in an error and a refresh loads again") {
        val repository = FakeRatesRepository().apply {
            rateHistory = Result.failure(IllegalStateException("offline"))
        }
        val store = createStore(repository)
        store.state.quotes.shouldBeInstanceOf<Resource.Error>()

        val quotes = listOf(rateQuote())
        repository.rateHistory = Result.success(quotes)
        store.accept(CurrencyDetailsStore.Intent.OnRefresh)

        repository.requestedHistory shouldBe listOf("USD" to "EUR", "USD" to "EUR")
        store.state.quotes shouldBe Resource.Data(quotes)
        store.state.name shouldBe "Euro"
    }

    test("an empty history stays data") {
        val repository = FakeRatesRepository().apply { rateHistory = Result.success(emptyList()) }

        val store = createStore(repository)

        store.state.quotes shouldBe Resource.Data(emptyList())
    }

    test("back publishes navigate back") {
        runTest(testDispatcher) {
            val store = createStore(FakeRatesRepository())
            var label: CurrencyDetailsStore.Label? = null
            val job = launch { label = store.labels.first() }

            store.accept(CurrencyDetailsStore.Intent.OnBack)
            job.join()

            label shouldBe CurrencyDetailsStore.Label.NavigateBack
        }
    }

})
