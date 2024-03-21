package com.example.mp_draft10.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mp_draft10.database.AddNewUserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MoodData(
    val date: LocalDate,
    val moodRating: Int,
    val moodObjects: List<String>,
    val symptomObjects: List<String>
)

@Composable
fun InsightsScreen(addNewUserViewModel: AddNewUserViewModel = viewModel()) {
    var moodRatingsMap by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }
//    var moodObjects by remember { mutableListOf<String>(emptyList<>()) }
//    val moodAndSymptoms = addNewUserViewModel.moodAndSymptoms.observeAsState(initial = emptyList())
    var moodAndSymptomsList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(key1 = "moodRatings") {
        moodRatingsMap = addNewUserViewModel.fetchMoodRatingsForPast7Days()
//        moodObjects = addNewUserViewModel.fetchMoodAndSymptomsForPast30Days()
    }


    LaunchedEffect(Unit) {
        moodAndSymptomsList = addNewUserViewModel.fetchMoodAndSymptomsForPast30Days()
    }

    var listForChart = countOccurrencesInList(moodAndSymptomsList)

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp) // This applies rounded corners to the Card
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp) // Adjust this value to increase/decrease padding from the top inside the Card
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Mood Ratings for Past 7 Days",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp) // Adds some space between the text and the chart or "no data" message
                    )

//                    if (moodRatingsMap.isNotEmpty()) {
                        MoodChart(moodRatingsMap)
//                    } else {
//                        Text(text = "No data", style = MaterialTheme.typography.bodyMedium)
//                    }

                    MoodAndSymptomsChart(listForChart)

                }
            }
        }
    }
}

@Composable
fun MoodAndSymptomsChart(moodAndSymptomsCounts: Map<String, Int>) {
    val fixedBarCount = 5 // Always display 5 bars for the top 5 moods and symptoms
    val maxCount = moodAndSymptomsCounts.values.maxOrNull() ?: 1

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Top 5 Mood and Symptoms Count", modifier = Modifier.padding(bottom = 8.dp))

        Canvas(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            val chartWidth = size.width
            val steps = moodAndSymptomsCounts.size
            val drawableHeight = size.height - 30.dp.toPx()
            val yStep = drawableHeight / steps
            val chartHeight = size.height - 30.dp.toPx()

            // Use a fixed bar width and calculate the spacing based on the fixed number of bars
            val barWidth = chartWidth / (fixedBarCount * 2).toFloat()
            val spaceBetweenBars = barWidth / 2
            var startingPoint = spaceBetweenBars

            val labelPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 30f
                textAlign = android.graphics.Paint.Align.CENTER
            }

            val countPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 24f
                textAlign = android.graphics.Paint.Align.CENTER
            }

            // Draw Horizontal lines
            for (i in 0 until steps+1) {
                val y = chartHeight - yStep * i
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(chartWidth, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw Y Axis
            drawLine(
                color = Color.Gray,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            // Draw X Axis
            drawLine(
                color = Color.Gray,
                start = Offset(x = 0f, y = chartHeight),
                end = Offset(x = chartWidth, y = chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            if (moodAndSymptomsCounts.isNotEmpty()) {
                moodAndSymptomsCounts.entries
                    .sortedByDescending { it.value }
                    .take(fixedBarCount)
                    .forEachIndexed { index, entry ->
                        val (mood, count) = entry
                        val barHeight = (count.toFloat() / maxCount) * (size.height - 60.dp.toPx())

                        drawRect(
                            color = Color.Blue,
                            topLeft = Offset(startingPoint, size.height - barHeight - 30.dp.toPx()),
                            size = Size(barWidth, barHeight)
                        )

                        drawContext.canvas.nativeCanvas.drawText(
                            mood,
                            startingPoint + barWidth / 2,
                            size.height - 5.dp.toPx(),
                            labelPaint
                        )

                        drawContext.canvas.nativeCanvas.drawText(
                            count.toString(),
                            startingPoint + barWidth / 2,
                            size.height - barHeight - 35.dp.toPx(),
                            countPaint
                        )
                        startingPoint += barWidth + spaceBetweenBars
                    }
            }

        }
    }
}


@Composable
fun MoodChart(moodRatings: Map<LocalDate, Int>) {
    val sortedRatings = moodRatings.entries.sortedBy { it.key }
    val dates = sortedRatings.map { it.key }
    val ratings = sortedRatings.map { it.value }
    val minValue = 1

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(start = 40.dp, top = 16.dp, end = 16.dp, bottom = 40.dp)
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val xStep = chartWidth / (dates.size - 1)
        val drawableHeight = size.height - 32.dp.toPx() // Adjust for padding at top & bottom

        val steps = 10
        val yStep = drawableHeight / steps

        // Draw 10 horizontal lines for the Y-axis
        for (i in 0 until steps) {
            val y = drawableHeight - yStep * i
            drawLine(
                color = Color.LightGray,
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw Y Axis
        drawLine(
            color = Color.Gray,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = 0f, y = chartHeight - 32.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )

        // Draw X Axis
        drawLine(
            color = Color.Gray,
            start = Offset(x = 0f, y = chartHeight - 32.dp.toPx()),
            end = Offset(x = chartWidth, y = chartHeight - 32.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )

        drawYAxisLabels(1, 10, chartHeight)
        drawXAxisLabels(dates, chartWidth, chartHeight + 10.dp.toPx(), this)

        if (moodRatings.isNotEmpty()) {

            // Draw Data Points and Lines
            for (i in 1 until dates.size) {
                val prev = sortedRatings[i - 1]
                val current = sortedRatings[i]
                val x1 = xStep * (i - 1)
                val y1 = (chartHeight - 32.dp.toPx()) - (prev.value.toFloat() - minValue) * yStep
                val x2 = xStep * i
                val y2 = (chartHeight - 32.dp.toPx()) - (current.value.toFloat() - minValue) * yStep


            }
            sortedRatings.forEachIndexed { index, entry ->
                val date = entry.key
                val rating = entry.value
                val x = xStep * index
                val y = chartHeight - 32.dp.toPx() - ((rating - minValue) * yStep)

                if (index < dates.size - 1) {
                    val nextRating = sortedRatings[index + 1].value
                    val nextY = chartHeight - 32.dp.toPx() - ((nextRating - minValue) * yStep)
                    drawLine(
                        color = Color.Blue,
                        start = Offset(x, y),
                        end = Offset(x + xStep, nextY),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Draw a dot for each data point
                drawCircle(
                    color = Color.Red,
                    center = Offset(x, y),
                    radius = 5f // Adjust the size of the dot as needed
                )
            }
        }
    }
}

fun DrawScope.drawYAxisLabels(start: Int, end: Int, chartHeight: Float) {
    val yAxisPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 30f // Consider adjusting the text size if necessary
        textAlign = android.graphics.Paint.Align.RIGHT // Text is right-aligned to the x-coordinate
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    }

    // Calculate the step height for each label on the Y-axis
    val yAxisStep = (chartHeight - 32.dp.toPx()) / end

    for (i in start..end) {
        val y = chartHeight - 15.dp.toPx() - (yAxisStep * i) + (yAxisPaint.textSize / 3) // Center the text vertically

        val x = -10.dp.toPx()

        drawContext.canvas.nativeCanvas.drawText(
            "$i",
            x, // This x-coordinate moves the labels to the left of the Y-axis.
            y, // y-coordinate for the label position
            yAxisPaint
        )
    }
}

fun drawXAxisLabels(dates: List<LocalDate>, chartWidth: Float, startY: Float, scope: DrawScope) {
    val dateFormatter = DateTimeFormatter.ofPattern("E", Locale.getDefault())
    val xStep = chartWidth / (dates.size.coerceAtLeast(1).toFloat())

    dates.forEachIndexed { index, date ->
        val x = 230f * index
        // Draw the day labels under the X Axis
        scope.drawContext.canvas.nativeCanvas.drawText(
            dateFormatter.format(date).uppercase(Locale.getDefault()),
            x,
            startY,
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 30f
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }
}

fun countOccurrencesInList(moodAndSymptomsList: List<String>): Map<String, Int> {
    return moodAndSymptomsList.groupingBy { it }.eachCount()
}

@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    // Mock data for preview
    val mockData = mapOf(
        LocalDate.now().minusDays(6) to 7,
        LocalDate.now().minusDays(5) to 5,
//        LocalDate.now().minusDays(4) to 4,
//        LocalDate.now().minusDays(3) to 3,
//        LocalDate.now().minusDays(2) to 7,
//        LocalDate.now().minusDays(1) to 10,
        LocalDate.now() to 6
    )
    // Use the mock data for the MoodChart in the preview
        Column(
            modifier = Modifier
//                .fillMaxSize()
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

@Preview(showBackground = true)
@Composable
fun PreviewMoodAndSymptomsChart() {
    // Example data for preview
    val exampleData = mapOf(
        "Happy" to 7,
        "Sad" to 2,
        "Anxious" to 8,
        "Energized" to 2,
        "Lonely" to 5,
        "Loved" to 4,
    )
    MoodAndSymptomsChart(moodAndSymptomsCounts = exampleData)
}
