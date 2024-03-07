package com.example.mp_draft10.ui

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    // Add other properties here, e.g., events or tasks
    val events: List<String>
)
