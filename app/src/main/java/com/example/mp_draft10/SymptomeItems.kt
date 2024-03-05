package com.example.mp_draft10

import androidx.annotation.DrawableRes

val symptomItems = mutableListOf<SymptomItem>(
    SymptomItem("Sweating", R.drawable.circle_outline),
    SymptomItem("Abdominal Pain", R.drawable.circle_outline),
    SymptomItem("Headache", R.drawable.circle_outline),
    SymptomItem("Nausea", R.drawable.nausea),
    SymptomItem("Fatigue", R.drawable.fatigue),
    SymptomItem("Dizziness", R.drawable.dizziness),
    SymptomItem("Shaking", R.drawable.shaking),
    )

class SymptomItem (
    val title: String,
    @DrawableRes val icon: Int
){}
