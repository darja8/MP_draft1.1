package com.example.mp_draft10.ui
import androidx.annotation.DrawableRes
import com.example.mp_draft10.R

val moodItems = mutableListOf(
    MoodItem("Anxious", R.drawable.anxious,6),
    MoodItem("Happy", R.drawable.happy,7),
    MoodItem("Sad", R.drawable.sad,8),
    MoodItem("Depressed", R.drawable.depressed,9),
    MoodItem("Lonely", R.drawable.lonely,10),
    MoodItem("Stressed", R.drawable.stressed,11),
    MoodItem("Worried", R.drawable.worried,12),
    MoodItem("Upset", R.drawable.upset,13),
    MoodItem("Excited", R.drawable.excited,14)
)

class MoodItem(
    val title: String,
    @DrawableRes val icon: Int,
    val moodObjectId: Int
)
