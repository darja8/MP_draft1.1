package com.example.mp_draft10

import com.example.mp_draft10.ui.components.calendar.Week
import com.example.mp_draft10.ui.components.calendar.WeekDay
import com.example.mp_draft10.ui.components.calendar.WeekDayPosition
import com.example.mp_draft10.ui.components.calendar.displayText
import com.example.mp_draft10.ui.components.calendar.getWeekPageTitle
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

class CalendarTests {
    @Test
    fun `test week title within same month`() {
        val week = Week(
            days = listOf(
                WeekDay(date = LocalDate.of(2022, Month.MARCH, 1),WeekDayPosition.InDate),
                WeekDay(date = LocalDate.of(2022, Month.MARCH, 7),WeekDayPosition.InDate)
            )
        )
        val title = getWeekPageTitle(week)
        assertEquals("March 2022", title)
    }

    @Test
    fun `test week title across different months same year`() {
        val week = Week(
            days = listOf(
                WeekDay(date = LocalDate.of(2022, Month.MARCH, 28),WeekDayPosition.InDate),
                WeekDay(date = LocalDate.of(2022, Month.APRIL, 3),WeekDayPosition.InDate)
            )
        )
        val title = getWeekPageTitle(week)
        assertEquals("March - April 2022", title)
    }

    @Test
    fun `test week title across different years`() {
        val week = Week(
            days = listOf(
                WeekDay(date = LocalDate.of(2022, Month.DECEMBER, 26),WeekDayPosition.InDate),
                WeekDay(date = LocalDate.of(2023, Month.JANUARY, 1),WeekDayPosition.InDate)
            )
        )
        val title = getWeekPageTitle(week)
        assertEquals("December 2022 - January 2023", title)
    }

    @Test
    fun `test Month display text`() {
        val month = Month.MARCH
        assertEquals("Mar", month.displayText())
        assertEquals("March", month.displayText(short = false))
    }

    @Test
    fun `test DayOfWeek display text`() {
        val dayOfWeek = DayOfWeek.MONDAY
        assertEquals("Mon", dayOfWeek.displayText())
        assertEquals("MON", dayOfWeek.displayText(uppercase = true))
    }
}