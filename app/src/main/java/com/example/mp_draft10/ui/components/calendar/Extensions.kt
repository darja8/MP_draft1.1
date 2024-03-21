package com.example.mp_draft10.ui.components.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale


/**
 * Returns the first day of the week from the default locale.
 */
fun firstDayOfWeekFromLocale(): DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

/**
 * Returns a [LocalDate] at the start of the month.
 *
 * Complements [YearMonth.atEndOfMonth].
 */
fun YearMonth.atStartOfMonth(): LocalDate = this.atDay(1)

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)

fun DayOfWeek.daysUntil(other: DayOfWeek) = (7 + (other.value - value)) % 7
