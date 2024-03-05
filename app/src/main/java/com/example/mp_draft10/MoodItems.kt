package com.example.mp_draft10
import androidx.annotation.DrawableRes

val moodItems = mutableListOf(
    MoodItem("Anxious", R.drawable.anxious),
    MoodItem("Happy", R.drawable.happy),
    MoodItem("Sad", R.drawable.sad),
    MoodItem("Confused", R.drawable.confused),
    MoodItem("Depressed", R.drawable.depressed),
    MoodItem("Irritated", R.drawable.irritated),
    MoodItem("Lonely", R.drawable.lonely),
    MoodItem("Stressed", R.drawable.stressed),
    MoodItem("Worried", R.drawable.worried),
    MoodItem("Upset", R.drawable.upset),
    MoodItem("Loved", R.drawable.loved),
    MoodItem("Excited", R.drawable.excited)
)


class MoodItem(
    val title: String,
    @DrawableRes val icon: Int
)
