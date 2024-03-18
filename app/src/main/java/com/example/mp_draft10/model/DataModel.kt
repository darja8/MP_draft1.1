package com.example.mp_draft10.model

import java.time.LocalDate

// Define the data model
data class DayData(
    val date: LocalDate,
    val moodRatingScale: MoodRatingScale,
    val mood: List<Mood>,
    val symptoms: List<Symptom>
)

// Define mood enum
enum class Mood {
    ANXIOUS,
    HAPPY,
    SAD,
    DEPRESSED,
    LONELY,
    STRESSED,
    WORRIED,
    UPSET,
    EXCITED
}

// Define symptom enum
enum class Symptom {
    SWEATING,
    ABDOMINALPAIN,
    HEADACHE,
    NAUSEA,
    FATIGUE,
    DIZZY
}

enum class MoodRatingScale(val rating: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10)
}