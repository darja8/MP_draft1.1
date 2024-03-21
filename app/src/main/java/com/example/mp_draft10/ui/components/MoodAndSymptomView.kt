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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    selectedMoods: List<String>,
    selectedSymptoms: List<String>,
    onMoodsSelected: (List<String>) -> Unit,
    onSymptomsSelected: (List<String>) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
//            .height()
//            .padding(start = 4.dp, end = 4.dp)
    ) {
        Column(
            modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)

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
                            val updatedMoods = if (selectedMoods.contains(mood)) {
                                selectedMoods.toMutableList().also { it.remove(mood) }
                            } else {
                                selectedMoods.toMutableList().also { it.add(mood) }
                            }
                            onMoodsSelected(updatedMoods)
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
                            val updatedSymptoms = if (selectedSymptoms.contains(symptom)) {
                                selectedSymptoms.toMutableList().also { it.remove(symptom) }
                            } else {
                                selectedSymptoms.toMutableList().also { it.add(symptom) }
                            }
                            onSymptomsSelected(updatedSymptoms)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun MoodRatingSquareView(
    selectedMoodRating: Int,
    onMoodSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .height(90.dp)
            .padding(start = 4.dp, end = 4.dp)
    ) {
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
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                (1..10).forEach { mood ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = if (selectedMoodRating == mood) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onMoodSelected(mood)
                            }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mood.toString(),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
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
            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
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

// Mock data for previews
private val previewSelectedMoods = listOf("Happy", "Energetic")
private val previewSelectedSymptoms = listOf("Headache", "Nausea")
private val previewMoodRating = 5

private val previewMoodItems = listOf(
    MoodItem("Happy", R.drawable.happy,1),
    MoodItem("Sad", R.drawable.sad,1)
    // Add more items as needed
)
//
//private val previewSymptomItems = listOf(
//    SymptomItem("Headache", R.drawable.ic_headache),
//    SymptomItem("Nausea", R.drawable.ic_nausea)
//    // Add more items as needed
//)

// Ensure these resources (e.g., R.drawable.ic_happy) are available in your project
// or replace them with existing ones for the preview to work.

@Preview(showBackground = true)
@Composable
fun PreviewMoodAndSymptomSquareView() {
    // Applying a MaterialTheme wrapper for theme consistency
    MaterialTheme {
        MoodAndSymptomSquareView(
            selectedMoods = previewSelectedMoods,
            selectedSymptoms = previewSelectedSymptoms,
            onMoodsSelected = {},
            onSymptomsSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMoodRatingSquareView() {
    MaterialTheme {
        MoodRatingSquareView(
            selectedMoodRating = previewMoodRating,
            onMoodSelected = {}
        )
    }
}