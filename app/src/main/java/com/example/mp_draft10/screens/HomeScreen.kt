package com.example.mp_draft10.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp_draft10.MoodItem
import com.example.mp_draft10.R
import com.example.mp_draft10.SymptomItem
import com.example.mp_draft10.moodItems
import com.example.mp_draft10.symptomItems
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("ResourceType")
@Composable
fun TodayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .wrapContentSize(Alignment.Center)
            .fillMaxHeight()
            .padding(top = 10.dp)
    ) {
        HorizontalCalendar()
//        Text(
//            text = "Today View",
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colorScheme.onBackground,
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            textAlign = TextAlign.Center,
//            fontSize = 25.sp
//        )
        MoodRatingSquareView(
            onMoodSelected = { mood ->
                // Handle the selected mood here, such as assigning it to the selected day on the calendar
            }
        )
        MoodAndSymptomSquareView(
            onMoodSelected = { /* Handle mood selection */ },
            onSymptomSelected = { /* Handle symptom selection */ }
        )
    }
}


@Composable
fun HorizontalCalendar() {
    val today = LocalDate.now()
    val startPeriod = today.minusDays(30)
    val endPeriod = today.plusDays(30)
    val totalDays = generateDateRange(startPeriod, endPeriod)
    val dateFormatter = DateTimeFormatter.ofPattern("d")
    val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE")
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
    val lazyListState = rememberLazyListState()
    val todayIndex = totalDays.indexOf(today).coerceAtLeast(0)

    // State to hold the current month based on scroll position
    var currentMonth by remember { mutableStateOf(monthFormatter.format(today)) }

    LaunchedEffect(key1 = today) {
        lazyListState.animateScrollToItem(index = todayIndex)
    }

    // Updating the month displayed as user scrolls
    val visibleItemIndex = lazyListState.firstVisibleItemIndex
    LaunchedEffect(key1 = visibleItemIndex) {
        totalDays.getOrNull(visibleItemIndex)?.let { date ->
            currentMonth = monthFormatter.format(date)
        }
    }

    Column{
        Text(
            text = currentMonth,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        LazyRow(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
            items(items = totalDays, key = { it }) { date ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = dayOfWeekFormatter.format(date).uppercase(Locale.ROOT),
                        color = if (date.isEqual(today)) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = if (date.isEqual(today)) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = dateFormatter.format(date),
                            color = if (date.isEqual(today)) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

fun generateDateRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val daysBetween = ChronoUnit.DAYS.between(startDate, endDate).toInt()
    return List(daysBetween) { startDate.plusDays(it.toLong()) }
}
@Composable
fun MoodRatingSquareView(onMoodSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
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
                fontSize = 20.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.thumb_up_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.thumb_down_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
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
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .height(370.dp)
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
fun MoodItemView(moodItem: MoodItem) {
    Box(
        modifier = Modifier
            .clickable {}
            .padding(horizontal = 1.dp, vertical = 3.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 1.dp, vertical = 1.dp)
    ) {Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {}
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Icon(
            painter = painterResource(id = moodItem.icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp) // Adjust size as needed
        )
        Spacer(modifier = Modifier.width(2.dp)) // Add spacing between icon and text
        Text(
            text = moodItem.title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        }
    }

}

@Composable
fun SymptomItemView(symptomItem: SymptomItem) {
    Box(
        modifier = Modifier
            .clickable {}
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 1.dp, vertical = 1.dp)
    ) {
        Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {}
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Icon(
            painter = painterResource(id = symptomItem.icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(2.dp)) // Add spacing between icon and text
        Text(
            text = symptomItem.title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }}

}




@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    MaterialTheme{
        TodayScreen()
    }
}