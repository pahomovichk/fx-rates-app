package com.fxrates.data.repository

import com.fxrates.data.remote.FrankfurterApi
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Instant

class RatesRepositoryImplTest : FunSpec({

    val clock = FixedClock(Instant.parse("2026-11-04T12:00:00Z"))

    test("a failed time series falls back to the current working day") {
        runTest {
            val requestedPaths = mutableListOf<String>()
            val repository = createRepository(
                clock = clock,
                requestedPaths = requestedPaths,
                timeSeriesStatus = HttpStatusCode.InternalServerError,
            )

            val rate = repository.getLatestRates(base = "USD").single()

            requestedPaths.any { path -> path.contains("..") } shouldBe true
            requestedPaths.any { path -> path.endsWith("/latest") } shouldBe true
            rate.code shouldBe "EUR"
            rate.name shouldBe "Euro"
            rate.date shouldBe LocalDate(2026, 11, 3)
            rate.rate shouldBe "0.89453".toBigDecimal()
            rate.previousRate shouldBe null
        }
    }

    test("a successful time series does not request the current working day") {
        runTest {
            val requestedPaths = mutableListOf<String>()
            val repository = createRepository(
                clock = clock,
                requestedPaths = requestedPaths,
                timeSeriesStatus = HttpStatusCode.OK,
            )

            val rate = repository.getLatestRates(base = "USD").single()

            requestedPaths.any { path -> path.endsWith("/latest") } shouldBe false
            rate.date shouldBe LocalDate(2026, 11, 3)
            rate.rate shouldBe "0.89158".toBigDecimal()
            rate.previousRate shouldBe "0.89509".toBigDecimal()
        }
    }

})

private class FixedClock(private val instant: Instant) : Clock {

    override fun now(): Instant = instant

}

private fun createRepository(
    clock: Clock,
    requestedPaths: MutableList<String>,
    timeSeriesStatus: HttpStatusCode,
): RatesRepositoryImpl {
    val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    val client = HttpClient(MockEngine) {
        expectSuccess = true
        defaultRequest {
            url("https://api.frankfurter.dev/v1/")
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        engine {
            addHandler { request ->
                val path = request.url.encodedPath
                requestedPaths += path
                when {
                    path.endsWith("/currencies") -> {
                        respond(content = """{"EUR":"Euro"}""", headers = jsonHeaders)
                    }
                    path.endsWith("/latest") -> {
                        respond(
                            content = """{"base":"USD","date":"2026-11-03","rates":{"EUR":0.89453}}""",
                            headers = jsonHeaders,
                        )
                    }
                    path.contains("..") -> {
                        respond(
                            content = TimeSeriesBody,
                            status = timeSeriesStatus,
                            headers = jsonHeaders,
                        )
                    }
                    else -> {
                        respond(content = """{"message":"not found"}""", status = HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
    return RatesRepositoryImpl(api = FrankfurterApi(client), converter = RatesConverter(), clock = clock)
}

private val TimeSeriesBody = """
    {"base":"USD","start_date":"2026-10-28","end_date":"2026-11-03",
     "rates":{"2026-11-03":{"EUR":0.89158},"2026-10-30":{"EUR":0.89509}}}
""".trimIndent()
