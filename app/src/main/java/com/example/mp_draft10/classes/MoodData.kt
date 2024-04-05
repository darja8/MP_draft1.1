package com.example.mp_draft10.classes

import java.time.LocalDate

data class MoodData(
    val date: LocalDate,
    val moodRating: Int,
    val moodObjects: List<String>,
    val symptomObjects: List<String>
)
