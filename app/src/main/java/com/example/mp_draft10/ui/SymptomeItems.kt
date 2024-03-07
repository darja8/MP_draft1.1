package com.example.mp_draft10.ui

import androidx.annotation.DrawableRes
import com.example.mp_draft10.R

val symptomItems = mutableListOf<SymptomItem>(
    SymptomItem("Sweating", R.drawable.circle_outline),
    SymptomItem("Abdominal Pain", R.drawable.circle_outline),
    SymptomItem("Headache", R.drawable.circle_outline),
    SymptomItem("Nausea", R.drawable.nausea),
    SymptomItem("Fatigue", R.drawable.fatigue),
    SymptomItem("Dizziness", R.drawable.dizziness),
    )

class SymptomItem (
    val title: String,
    @DrawableRes val icon: Int
){}
