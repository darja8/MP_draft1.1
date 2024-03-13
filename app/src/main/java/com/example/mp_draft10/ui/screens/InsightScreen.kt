package com.example.mp_draft10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate


data class MoodData(val date: LocalDate, val rating: Double)

@Composable
fun InsightsScreen() {
    // Dummy data for demonstration
    val moodData = listOf(
        MoodData(LocalDate.now().minusDays(6), 3.0),
        MoodData(LocalDate.now().minusDays(5), 4.0),
        MoodData(LocalDate.now().minusDays(4), 5.0),
        MoodData(LocalDate.now().minusDays(3), 6.0),
        MoodData(LocalDate.now().minusDays(2), 7.0),
        MoodData(LocalDate.now().minusDays(1), 8.0),
        MoodData(LocalDate.now(), 9.0)
    )

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
        MoodChart(moodData)
    }
}

@Composable
fun MoodChart(data: List<MoodData>) {
    val formattedData = data.map { Pair(it.date.toString(), it.rating) }
//    Chart(formattedData, LineSeries())
}

@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    InsightsScreen()
}