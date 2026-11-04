package com.fxrates.data.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class HistoryWindowStartTest : FunSpec({

    test("the history window starts thirty calendar days before today") {
        historyWindowStart(today = LocalDate(2026, 11, 4)) shouldBe LocalDate(2026, 10, 5)
    }

})
