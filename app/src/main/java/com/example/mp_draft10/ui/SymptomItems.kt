package com.example.mp_draft10.ui

import androidx.annotation.DrawableRes
import com.example.mp_draft10.R

/**
 * List of Symptoms used on home screen
 * all images have been downloaded from https://www.freepik.com/author/chattapat/icons/generic-flat_1417?t=f#from_element=resource_detail
 *
 */

val symptomItems = mutableListOf<SymptomItem>(
    SymptomItem("Sweating", R.drawable.sweating, 0),
    SymptomItem("Abdominal Pain", R.drawable.abdominalpain, 1),
    SymptomItem("Headache", R.drawable.headache,2),
    SymptomItem("Nausea", R.drawable.nausea,3),
    SymptomItem("Fatigue", R.drawable.fatigue,4),
    SymptomItem("Dizziness", R.drawable.dizziness,5),
    )

class SymptomItem (
    val title: String,
    @DrawableRes val icon: Int,
    val symptomObjectId: Int
){}
