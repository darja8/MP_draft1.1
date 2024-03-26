package com.example.mp_draft10.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.database.AddNewUserViewModel
import com.example.mp_draft10.ui.components.MainScreenScaffold
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MoodData(
    val date: LocalDate,
    val moodRating: Int,
    val moodObjects: List<String>,
    val symptomObjects: List<String>
)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun InsightsScreen(
    addNewUserViewModel: AddNewUserViewModel = viewModel(),
    navController: NavHostController
) {
    var moodRatingsMap by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }
    var moodAndSymptomsList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(key1 = "moodRatings") {
        moodRatingsMap = addNewUserViewModel.fetchMoodRatingsForPast7Days()
    }

    LaunchedEffect(Unit) {
        moodAndSymptomsList = addNewUserViewModel.fetchMoodAndSymptomsForPast30Days()
    }

    val listForChart = countOccurrencesInList(moodAndSymptomsList)

    MainScreenScaffold(navController = navController)
    {
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
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    MoodChart(moodRatingsMap)

                    Text(
                        text = "5 most logged Moods and Symptoms for past 30 days",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp) // Adds some space between the text and the chart or "no data" message
                    )
                    MoodAndSymptomsChart(listForChart)
                }
            }
        }
    }
}

@Composable
fun MoodAndSymptomsChart(moodAndSymptomsCounts: Map<String, Int>) {
    val fixedBarCount = 5
    val primaryColor = MaterialTheme.colorScheme.primary // This ensures it adapts to theme changes
    val onBackground = MaterialTheme.colorScheme.onBackground

    val maxCount = moodAndSymptomsCounts.values.maxOrNull() ?: 1

    Column(modifier = Modifier.padding(16.dp)) {


        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)) {
            val chartWidth = size.width
            val steps = moodAndSymptomsCounts.size
            val drawableHeight = size.height - 30.dp.toPx()
            val yStep = drawableHeight / steps
            val chartHeight = size.height - 30.dp.toPx()

            // Use a fixed bar width and calculate the spacing based on the fixed number of bars
            val barWidth = chartWidth / (fixedBarCount * 2).toFloat()
            val spaceBetweenBars = barWidth / 1.5
            var startingPoint = spaceBetweenBars

            val labelPaint = android.graphics.Paint().apply {
                color = onBackground.toArgb()
                textSize = 40f
                textAlign = android.graphics.Paint.Align.CENTER
            }

            val countPaint = android.graphics.Paint().apply {
                color = onBackground.toArgb()
                textSize = 30f
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
                            color = primaryColor,
                            topLeft = Offset(startingPoint.toFloat(), size.height - barHeight - 30.dp.toPx()),
                            size = Size(barWidth, barHeight)
                        )

                        drawContext.canvas.nativeCanvas.drawText(
                            mood,
                            (startingPoint + barWidth / 2).toFloat(),
                            size.height - 5.dp.toPx(),
                            labelPaint
                        )

                        drawContext.canvas.nativeCanvas.drawText(
                            count.toString(),
                            (startingPoint + barWidth / 2).toFloat(),
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
    val minValue = 1
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onBackground = MaterialTheme.colorScheme.onBackground

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

        drawYAxisLabels(1, 10, chartHeight, onBackground)
        drawXAxisLabels(dates, chartWidth, chartHeight + 1.dp.toPx(), this, onBackground)

        if (moodRatings.isNotEmpty()) {
            sortedRatings.forEachIndexed { index, entry ->
                val rating = entry.value
                val x = xStep * index
                val y = chartHeight - 32.dp.toPx() - ((rating - minValue) * yStep)

                if (index < dates.size - 1) {
                    val nextRating = sortedRatings[index + 1].value
                    val nextY = chartHeight - 32.dp.toPx() - ((nextRating - minValue) * yStep)
                    drawLine(
                        color = primaryColor,
                        start = Offset(x, y),
                        end = Offset(x + xStep, nextY),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Draw a dot for each data point
                drawCircle(
                    color = secondaryColor,
                    center = Offset(x, y),
                    radius = 5f // Adjust the size of the dot as needed
                )
            }
        }
    }
}


fun DrawScope.drawYAxisLabels(start: Int, end: Int, chartHeight: Float, themeColor: Color) {

    val yAxisPaint = android.graphics.Paint().apply {
        color = themeColor.toArgb()
        textSize = 40f // Consider adjusting the text size if necessary
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

fun drawXAxisLabels(dates: List<LocalDate>, chartWidth: Float, startY: Float, scope: DrawScope, themeColor: Color) {
    val dateFormatter = DateTimeFormatter.ofPattern("E", Locale.getDefault())
    // Calculate the space available for each data point, considering one less gap than the number of points
    val xStep = if (dates.size > 1) chartWidth / (dates.size - 1) else chartWidth
    val labelPaint = android.graphics.Paint().apply {
        color = themeColor.toArgb()
        textSize = 35f
        textAlign = android.graphics.Paint.Align.CENTER // This ensures the text is centered at the calculated X position
    }

    dates.forEachIndexed { index, date ->
        // Calculate the center position for each label based on its index
        val x = xStep * index
        scope.drawContext.canvas.nativeCanvas.drawText(
            dateFormatter.format(date).uppercase(Locale.getDefault()),
            x,
            startY,
            labelPaint
        )
    }
}


fun countOccurrencesInList(moodAndSymptomsList: List<String>): Map<String, Int> {
    return moodAndSymptomsList.groupingBy { it }.eachCount()
}

@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    val mockData = mapOf(
        LocalDate.now().minusDays(6) to 7,
        LocalDate.now().minusDays(5) to 5,
//        LocalDate.now().minusDays(4) to 4,
//        LocalDate.now().minusDays(3) to 3,
        LocalDate.now().minusDays(2) to 7,
//        LocalDate.now().minusDays(1) to 10,
        LocalDate.now() to 6
    )
        Column(
            modifier = Modifier
//                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MoodChart(moodRatings = mockData)
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewMoodAndSymptomsChart() {
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
