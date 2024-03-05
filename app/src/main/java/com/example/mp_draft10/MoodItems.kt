package com.example.mp_draft10
import androidx.annotation.DrawableRes

val moodItems = mutableListOf(
    MoodItem("Anxious", R.drawable.circle_outline),
    MoodItem("Happy", R.drawable.circle_outline),
    MoodItem("Sad", R.drawable.circle_outline),
    MoodItem("Confused", R.drawable.circle_outline),
    MoodItem("Depressed", R.drawable.circle_outline),
    MoodItem("Irritated", R.drawable.circle_outline),
    MoodItem("Lonely", R.drawable.circle_outline),
    MoodItem("Stressed", R.drawable.circle_outline),
    MoodItem("Worried", R.drawable.circle_outline),
    MoodItem("Uncomfortable", R.drawable.circle_outline),
    MoodItem("Satisfied", R.drawable.circle_outline),
    MoodItem("Loved", R.drawable.circle_outline),
    MoodItem("Excited", R.drawable.circle_outline)
)


class MoodItem(
    val title: String,
    @DrawableRes val icon: Int
)
