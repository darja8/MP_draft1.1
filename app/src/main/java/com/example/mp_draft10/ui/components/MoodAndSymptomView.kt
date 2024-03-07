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
    onMoodSelected: (String) -> Unit,
    onSymptomSelected: (String) -> Unit
) {
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
                modifier = Modifier
                    .padding(start = 13.dp, top = 16.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(1.dp), // Increased spacing between items
                verticalArrangement = Arrangement.spacedBy(1.dp), // Increased spacing between rows
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                moodItems.forEach { moodItem ->
                    MoodItemView(moodItem = moodItem)
                }
            }
//            Spacer(modifier = Modifier.height(1.dp)) // Add Spacer to separate Mood and Symptoms
            Text(
                text = "Symptoms",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 13.dp, top = 16.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(1.dp), // Increased spacing between items
                verticalArrangement = Arrangement.spacedBy(1.dp), // Increased spacing between rows
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                symptomItems.forEach { symptomItem ->
                    SymptomItemView(symptomItem = symptomItem)
                }
            }
        }
    }
}

@Composable
fun MoodRatingSquareView(onMoodSelected: (Int) -> Unit) {
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
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How do you feel today?",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer)
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
                )
                Icon(
                    painter = painterResource(id = R.drawable.thumb_up_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
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
                                onMoodSelected(mood)
                            } // Handle mood selection
                            .padding(4.dp), // Add padding around each square
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mood.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MoodItemView(moodItem: MoodItem) {
    Button(
        onClick = {},
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondary)
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
fun SymptomItemView(symptomItem: SymptomItem) {
    Button(
        onClick = {},
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondary)
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