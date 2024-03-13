package com.example.mp_draft10.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp_draft10.R
import com.example.mp_draft10.ui.MoodItem
import com.example.mp_draft10.ui.SymptomItem
import com.example.mp_draft10.ui.moodItems
import com.example.mp_draft10.ui.symptomItems

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodAndSymptomSquareView(
    onMoodsSelected: (List<String>) -> Unit,
    onSymptomsSelected: (List<String>) -> Unit
) {
    val selectedMoods = remember { mutableStateListOf<String>() }
    val selectedSymptoms = remember { mutableStateListOf<String>() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .height(400.dp)
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Mood",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 13.dp, top = 16.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                moodItems.forEach { moodItem ->
                    MoodItemView(
                        moodItem = moodItem,
                        isSelected = selectedMoods.contains(moodItem.title),
                        onMoodSelected = { mood ->
                            if (selectedMoods.contains(mood)) {
                                selectedMoods.remove(mood)
                            } else {
                                selectedMoods.add(mood)
                            }
                            onMoodsSelected(selectedMoods) // Notify the listener with the updated list
                        }
                    )
                }
            }
            Text(
                text = "Symptoms",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 13.dp, top = 16.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                symptomItems.forEach { symptomItem ->
                    SymptomItemView(
                        symptomItem = symptomItem,
                        isSelected = selectedSymptoms.contains(symptomItem.title),
                        onSymptomSelected = { symptom ->
                            if (selectedSymptoms.contains(symptom)) {
                                selectedSymptoms.remove(symptom)
                            } else {
                                selectedSymptoms.add(symptom)
                            }
                            onSymptomsSelected(selectedSymptoms) // Notify the listener with the updated list
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MoodRatingSquareView(onMoodSelected: (Int) -> Unit) {
    var selectedMood by remember { mutableStateOf(0)} // State variable to track selected mood rating

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .height(110.dp) // Adjusted height for horizontal layout
            .padding(start = 4.dp, end = 4.dp)
    ) {
        // Add content inside the Box to display within the rounded square
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How do you feel today?",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.thumb_down_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                        .clickable {
                            // Update selected mood and notify the listener
                            selectedMood = 0
                            onMoodSelected(selectedMood)
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.thumb_up_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                        .clickable {
                            // Update selected mood and notify the listener
                            selectedMood = 11
                            onMoodSelected(selectedMood)
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                (1..10).forEach { mood ->
                    Box(
                        modifier = Modifier
                            .size(30.dp) // Adjust size of colored square
                            .background(
                                MaterialTheme.colorScheme.onSecondary,
                                shape = RoundedCornerShape(8.dp)
                            ) // Gray background for each square
                            .clickable {
                                // Update selected mood and notify the listener
                                selectedMood = mood
                                onMoodSelected(selectedMood)
                            } // Handle mood selection
                            .padding(4.dp), // Add padding around each square
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mood.toString(),
                            color = if (selectedMood == mood) {
                                MaterialTheme.colorScheme.onErrorContainer //selected rating from 1 to 10
                            } else {
                                MaterialTheme.colorScheme.onSecondaryContainer // Change color of selected mood
                            },
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun MoodItemView(
    moodItem: MoodItem,
    isSelected: Boolean,
    onMoodSelected: (String) -> Unit
) {
    Button(
        onClick = { onMoodSelected(moodItem.title) },
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Image(
            painter = painterResource(id = moodItem.icon),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.width(2.dp)) // Add spacing between icon and text
        Text(
            text = moodItem.title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SymptomItemView(
    symptomItem: SymptomItem,
    isSelected: Boolean,
    onSymptomSelected: (String) -> Unit
) {
    Button(
        onClick = { onSymptomSelected(symptomItem.title) },
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Image(
            painter = painterResource(id = symptomItem.icon),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.width(2.dp)) // Add spacing between icon and text
        Text(
            text = symptomItem.title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start
        )
    }
}