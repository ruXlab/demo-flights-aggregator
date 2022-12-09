package com.example.demosb.testutils

import java.time.LocalDate
import java.time.Month

// Say in our domain we got to work with dates often.
// So let's make this easy for ourselves using DSL for frequent usecases

// In this example we'd be using LocalDate DSL generator since humans tend to read Day / Month / Year rather
// than LocalDate.of(2011, 12, 10)
// These two are equivalent
//    - LocalDate.of(2011, 12, 10)
//    10 / DECEMBER / 2011

data class DayAndMonth(val day: Int, val month: Month)

infix operator fun Int.div(month: Month): DayAndMonth =
    DayAndMonth(day = this, month = month)

infix operator fun DayAndMonth.div(year: Int): LocalDate =
    LocalDate.of(year, this.month, this.day) 