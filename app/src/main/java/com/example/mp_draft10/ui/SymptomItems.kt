package com.example.mp_draft10.ui

import androidx.annotation.DrawableRes
import com.example.mp_draft10.R

val symptomItems = mutableListOf<SymptomItem>(
    SymptomItem("Sweating", R.drawable.circle_outline, 0),
    SymptomItem("Abdominal Pain", R.drawable.circle_outline, 1),
    SymptomItem("Headache", R.drawable.circle_outline,2),
    SymptomItem("Nausea", R.drawable.nausea,3),
    SymptomItem("Fatigue", R.drawable.fatigue,4),
    SymptomItem("Dizziness", R.drawable.dizziness,5),
    )

class SymptomItem (
    val title: String,
    @DrawableRes val icon: Int,
    val symptomObjectId: Int
){}
