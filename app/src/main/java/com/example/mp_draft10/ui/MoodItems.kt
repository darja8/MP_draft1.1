package com.example.mp_draft10.ui
import androidx.annotation.DrawableRes
import com.example.mp_draft10.R

val moodItems = mutableListOf(
    MoodItem("Anxious", R.drawable.anxious),
    MoodItem("Happy", R.drawable.happy),
    MoodItem("Sad", R.drawable.sad),
    MoodItem("Depressed", R.drawable.depressed),
    MoodItem("Lonely", R.drawable.lonely),
    MoodItem("Stressed", R.drawable.stressed),
    MoodItem("Worried", R.drawable.worried),
    MoodItem("Upset", R.drawable.upset),
    MoodItem("Excited", R.drawable.excited)
)

class MoodItem(
    val title: String,
    @DrawableRes val icon: Int
)
