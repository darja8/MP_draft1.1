package com.example.mp_draft10.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mp_draft10.database.AddNewUserViewModel
import java.time.LocalDate


data class MoodData(
    val date: LocalDate,
    val moodRating: Int,
    val moodObjects: List<String>,
    val symptomObjects: List<String>
)

@Composable
fun InsightsScreen(addNewUserViewModel: AddNewUserViewModel = viewModel()) {
    var moodRatingsMap by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }

    LaunchedEffect(key1 = "moodRatings") {
        moodRatingsMap = addNewUserViewModel.fetchMoodRatingsForPast7Days()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mood Ratings for Past 7 Days",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        MoodChart(moodRatingsMap)
    }
}

@Composable
fun MoodChart(moodRatings: Map<LocalDate, Int>) {
    if (moodRatings.isNotEmpty()) {
        val sortedRatings = moodRatings.entries.sortedBy { it.key }
        val dates = sortedRatings.map { it.key }
        val ratings = sortedRatings.map { it.value }
        val minValue = ratings.minOrNull() ?: 0
        val maxValue = ratings.maxOrNull() ?: 0

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(start = 40.dp, top = 16.dp, end = 16.dp, bottom = 40.dp)) { // Adjust padding for axes labels
            val chartWidth = size.width
            val chartHeight = size.height
            val xStep = chartWidth / (dates.size - 1)
            val yStep = (chartHeight - 32.dp.toPx()) / (maxValue - minValue).coerceAtLeast(1) // Adjust Y step for padding

            // Draw Y Axis
            drawLine(
                color = Color.Gray,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = chartHeight - 32.dp.toPx()), // Adjust height for padding
                strokeWidth = 2.dp.toPx()
            )

            // Draw X Axis
            drawLine(
                color = Color.Gray,
                start = Offset(x = 0f, y = chartHeight - 32.dp.toPx()), // Adjust for padding
                end = Offset(x = chartWidth, y = chartHeight - 32.dp.toPx()), // Adjust for padding
                strokeWidth = 2.dp.toPx()
            )

            // Draw Data Points and Lines
            for (i in 1 until dates.size) {
                val prev = sortedRatings[i - 1]
                val current = sortedRatings[i]
                val x1 = xStep * (i - 1)
                val y1 = (chartHeight - 32.dp.toPx()) - (prev.value.toFloat() - minValue) * yStep // Adjust for padding
                val x2 = xStep * i
                val y2 = (chartHeight - 32.dp.toPx()) - (current.value.toFloat() - minValue) * yStep // Adjust for padding

                // Draw Line Between Points
                drawLine(
                    Color.Blue,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 4.dp.toPx()
                )
            }

            // Draw Labels on Y Axis
            val labelStep = (maxValue - minValue) / 4 // For simplicity, dividing Y-axis into 4 segments
            for (i in 0..4) {
                val yLabel = minValue + (labelStep * i)
                val yPosition = chartHeight - 32.dp.toPx() - (yLabel.toFloat() - minValue) * yStep // Adjust for padding

                drawContext.canvas.nativeCanvas.drawText(
                    yLabel.toString(),
                    0f,
                    yPosition + 5.dp.toPx(), // Small adjustment for text alignment
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 30f
                    }
                )
            }

            // Draw Labels on X Axis (Simplified to show only first and last date)
            drawContext.canvas.nativeCanvas.drawText(
                dates.first().toString(),
                0f,
                chartHeight, // Position text at the bottom
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                }
            )

            drawContext.canvas.nativeCanvas.drawText(
                dates.last().toString(),
                chartWidth - 120.dp.toPx(), // Rough adjustment to avoid clipping, adjust as needed
                chartHeight, // Position text at the bottom
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                }
            )
        }
    } else {
        Text("No mood data available for the past 7 days.")
    }
}



@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    // Mock data for preview
    val mockData = mapOf(
        LocalDate.now().minusDays(6) to 2,
        LocalDate.now().minusDays(5) to 5,
        LocalDate.now().minusDays(4) to 4,
        LocalDate.now().minusDays(3) to 3,
        LocalDate.now().minusDays(2) to 7,
        LocalDate.now().minusDays(1) to 8,
        LocalDate.now() to 6
    )
    // Use the mock data for the MoodChart in the preview
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mood Ratings for Past 7 Days (Preview)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            MoodChart(moodRatings = mockData)
        }
    }
}
