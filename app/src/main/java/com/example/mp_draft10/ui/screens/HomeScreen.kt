package com.example.mp_draft10.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.database.AddNewUserViewModel
import com.example.mp_draft10.ui.components.MoodAndSymptomSquareView
import com.example.mp_draft10.ui.components.MoodRatingSquareView
import com.example.mp_draft10.ui.components.calendar.WeekCalendar
import com.example.mp_draft10.ui.components.calendar.displayText
import com.example.mp_draft10.ui.components.calendar.getWeekPageTitle
import com.example.mp_draft10.ui.components.calendar.rememberFirstVisibleWeekAfterScroll
import com.google.firebase.auth.FirebaseAuth
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("ResourceType")
fun TodayScreen(navController: NavHostController, addNewUserViewModel: AddNewUserViewModel = hiltViewModel()) {

    val auth = FirebaseAuth.getInstance()

    // Store selected moods and symptoms
    var selectedMoods by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedSymptoms by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var selectedMoodRating by remember { mutableStateOf(0) }

    fun handleDaySelected(day: LocalDate) {
        selectedDay = day // Update the selected day
    }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Today") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            state = listState
        ) {
            item {
                CalendarSlide(
                    onDaySelected = { day ->
                        handleDaySelected(day)
                    },
                    close = {}
                )
            }

            item {
                MoodRatingSquareView(
                    onMoodSelected = { moodRating ->
                        selectedMoodRating = moodRating
                    }
                )
            }
            item {
                MoodAndSymptomSquareView(
                    onMoodsSelected = { moods ->
                        selectedMoods = moods
                    },
                    onSymptomsSelected = { symptoms ->
                        selectedSymptoms = symptoms
                    }
                )
            }
            item {
                Button(
                    onClick = {
                        addNewUserViewModel.saveMoodToFirestore(selectedDay, selectedMoods, selectedSymptoms, selectedMoodRating)
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(text = "Save Mood and Symptoms", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}


@Composable
fun CalendarSlide(
    onDaySelected: (LocalDate) -> Unit,
    close: () -> Unit = {}
){
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }
    Column(
        modifier = Modifier
            .height(140.dp)
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
        )
        WeekCalendar(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            state = state,
            dayContent = { day ->
                Day(day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                        onDaySelected(clicked) // Invoke the callback with the selected date
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
@Preview
@Composable
fun TodayScreenPreview() {
    MaterialTheme {
        TodayScreen(navController = rememberNavController())
    }
}
