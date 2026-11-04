package com.fxrates.data.repository

import com.fxrates.data.remote.FrankfurterApi
import com.fxrates.domain.model.CurrencyRate
import com.fxrates.domain.model.RateQuote
import com.fxrates.domain.repository.RatesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import me.tatarka.inject.annotations.Inject
import kotlin.time.Clock

@Inject
class RatesRepositoryImpl(
    private val api: FrankfurterApi,
    private val converter: RatesConverter,
    private val clock: Clock,
) : RatesRepository {

    override suspend fun getLatestRates(base: String): List<CurrencyRate> {
        return try {
            ratesFromTimeSeries(base)
        } catch (exception: CancellationException) {
            throw exception
        } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") exception: Exception) {
            getCurrentRates(base)
        }
    }

    private suspend fun ratesFromTimeSeries(base: String): List<CurrencyRate> = coroutineScope {
        // A week back always holds at least two business days, even around long holidays.
        val start = clock.todayIn(TimeZone.UTC).minus(DatePeriod(days = LOOKBACK_DAYS))
        val currencies = async { api.getCurrencies() }
        val timeSeries = async { api.getTimeSeries(base = base, start = start) }
        converter.toLatestRates(timeSeries = timeSeries.await(), currencies = currencies.await())
    }

    override suspend fun getCurrentRates(base: String): List<CurrencyRate> = coroutineScope {
        val currencies = async { api.getCurrencies() }
        val latest = async { api.getLatest(base = base) }
        converter.toCurrentRates(latest = latest.await(), currencies = currencies.await())
    }

    override suspend fun getRateHistory(base: String, code: String): List<RateQuote> {
        val start = historyWindowStart(today = clock.todayIn(TimeZone.UTC))
        val timeSeries = api.getTimeSeriesTo(base = base, start = start, currency = code)
        return converter.toQuotes(timeSeries = timeSeries, code = code)
    }

    private companion object {

        const val LOOKBACK_DAYS = 7

    }

}

/** Thirty calendar days before [today]. The history request leaves the end date open. */
internal fun historyWindowStart(today: LocalDate): LocalDate =
    today.minus(DatePeriod(days = HistoryLookbackDays))

private const val HistoryLookbackDays = 30
