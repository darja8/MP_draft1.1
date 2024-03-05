package com.example.mp_draft10

import androidx.annotation.DrawableRes

val symptomItems = mutableListOf<SymptomItem>(
    SymptomItem("Sweating", R.drawable.circle_outline),
    SymptomItem("Abdominal Pain", R.drawable.circle_outline),
    SymptomItem("Headache", R.drawable.circle_outline),
    SymptomItem("Nausea", R.drawable.circle_outline),
    SymptomItem("Fatigue", R.drawable.circle_outline),
    SymptomItem("Dizziness", R.drawable.circle_outline),
    SymptomItem("Shaking", R.drawable.circle_outline),
    )

class SymptomItem (
    val title: String,
    @DrawableRes val icon: Int
){}
