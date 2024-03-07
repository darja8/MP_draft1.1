package com.example.mp_draft10.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp_draft10.ui.components.MoodAndSymptomSquareView
import com.example.mp_draft10.ui.components.MoodRatingSquareView
import com.example.mp_draft10.ui.components.calendar.WeekCalendar
import com.example.mp_draft10.ui.components.calendar.displayText
import com.example.mp_draft10.ui.components.calendar.getWeekPageTitle
import com.example.mp_draft10.ui.components.calendar.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

        CalendarSlide()
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
fun CalendarSlide(close: () -> Unit = {}) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }
    Column(
        modifier = Modifier
//            .fillMaxSize()
            .height(150.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = currentDate,
        )
        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
        TopAppBar(
            elevation = 0.dp,
            title = { Text(text = getWeekPageTitle(visibleWeek)) },
            backgroundColor = MaterialTheme.colorScheme.background
//            navigationIcon = { NavigationIcon(onBackClick = close) },
        )
        WeekCalendar(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            state = state,
            dayContent = { day ->
                Day(day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                    }
                }
            },
        )
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.dayOfWeek.displayText(),
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = dateFormatter.format(date),
                fontSize = 14.sp,
                color = if (isSelected) Color.Black else Color.Black,
                fontWeight = FontWeight.Bold,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    MaterialTheme{
        TodayScreen()
    }
}