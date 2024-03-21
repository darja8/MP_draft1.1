package drawable

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("ResourceType")
fun TodayScreen(navController: NavHostController, addNewUserViewModel: AddNewUserViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()

    // Store selected moods and symptoms
    var selectedMoods by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedSymptoms by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var selectedMoodRating by remember { mutableStateOf(0) }

    fun handleDaySelected(day: LocalDate) {
        selectedDay = day // Update the selected day
    }

    val listState = rememberLazyListState()

    LaunchedEffect(selectedDay) {
        scope.launch {
            val moodData = addNewUserViewModel.fetchMoodDataFromSpecificDay(selectedDay)
            selectedMoods = moodData?.moodObjects ?: emptyList()
            selectedSymptoms = moodData?.symptomObjects ?: emptyList()
            selectedMoodRating = moodData?.moodRating ?: 0
        }
    }
    Scaffold(
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            state = listState
        ) {
            item {
                CalendarSlide(
                    onDaySelected = { day ->
                        handleDaySelected(day) // Update selected day and fetch mood data
                    },
                    onSettingsClicked = {
                        navController.navigate("settings") // Navigate to settings screen
                    },
                    close = {}
                )
            }

            item {
                MoodRatingSquareView(
                    selectedMoodRating = selectedMoodRating,
                    onMoodSelected = { rating ->
                        selectedMoodRating = rating
                        // Potentially save this selection
                    }
                )
            }
            item {
                MoodAndSymptomSquareView(
                    selectedMoods = selectedMoods,
                    selectedSymptoms = selectedSymptoms,
                    onMoodsSelected = { moods ->
                        selectedMoods = moods
                        // Potentially save these selections
                    },
                    onSymptomsSelected = { symptoms ->
                        selectedSymptoms = symptoms
                        // Potentially save these selections
                    }
                )
            }
            item {
                Button(
                    onClick = {
                        addNewUserViewModel.saveMoodToFirestore(selectedDay, selectedMoods, selectedSymptoms, selectedMoodRating)
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
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
    onSettingsClicked: () -> Unit, // Callback for when the settings icon is clicked
    close: () -> Unit = {}
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }
    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)

    Column(
        modifier = Modifier
            .height(120.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBar(
            elevation = 0.dp,
            title = { Text(text = getWeekPageTitle(visibleWeek), color = MaterialTheme.colorScheme.onBackground) },
            backgroundColor = MaterialTheme.colorScheme.background,
            actions = {
                // Place the settings IconButton here, inside the TopAppBar of the CalendarSlide
                IconButton(onClick = onSettingsClicked) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
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
    val today = LocalDate.now()
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
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Light,
            )
            // Check if the date is today to draw a circle around it
            if (date == today) {
                Box(
                    modifier = Modifier
                        .size(24.dp) // Adjust the size of the circle as needed
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .padding(2.dp), // Adjust padding to fit the circle
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dateFormatter.format(date),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground, // Ensure text color contrasts with the circle's background
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
                // Regular day text without the circle
                Text(
                    text = dateFormatter.format(date),
                    fontSize = 14.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                )
            }
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


//if (isSelected) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(5.dp)
//            .background(MaterialTheme.colorScheme.primary)
//            .align(Alignment.BottomCenter),
//    )
//}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview
@Composable
fun TodayScreenPreview() {
    MaterialTheme {
        TodayScreen(navController = rememberNavController())
    }
}

@Composable
@Preview(showBackground = true)
fun CalendarSlidePreview() {
    // Wrapping the preview in your app's theme, if you have one
    MaterialTheme {
        // Assuming your calendar needs a NavController, even though it won't be functional in the preview
        val navController = rememberNavController()

        // Providing dummy implementations for required callbacks
        CalendarSlide(
            onDaySelected = {},
            onSettingsClicked = {},
            close = {}
        )
    }
}