package com.example.mp_draft10.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.example.mp_draft10.ui.DisplaySavedAvatarAndColor
import com.example.mp_draft10.ui.components.MainScreenScaffold
import com.example.mp_draft10.ui.components.MoodAndSymptomSquareView
import com.example.mp_draft10.ui.components.MoodRatingSquareView
import com.example.mp_draft10.ui.components.calendar.WeekCalendar
import com.example.mp_draft10.ui.components.calendar.getWeekPageTitle
import com.example.mp_draft10.ui.components.calendar.rememberFirstVisibleWeekAfterScroll
import com.example.mp_draft10.ui.components.calendar.rememberWeekCalendarState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeParseException

@SuppressLint("ResourceType")
@Composable
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun TodayScreen(navController: NavHostController, addNewUserViewModel: AddNewUserViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()

    var selectedMoods by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedSymptoms by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var selectedMoodRating by remember { mutableStateOf(0) }

    LaunchedEffect(selectedDay) {
        scope.launch {
            val moodData = addNewUserViewModel.fetchMoodDataFromSpecificDay(selectedDay)
            selectedMoods = moodData?.moodObjects ?: emptyList()
            selectedSymptoms = moodData?.symptomObjects ?: emptyList()
            selectedMoodRating = moodData?.moodRating ?: 0
        }
    }

    MainScreenScaffold(navController = navController) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                CalendarSlide(
                    onDaySelected = { day ->
                        selectedDay = day // Update selected day and fetch mood data
                    },
                    navController = navController,
                    addNewUserViewModel = addNewUserViewModel
                )
            }

            item {
                MoodRatingSquareView(
                    selectedMoodRating = selectedMoodRating,
                    onMoodSelected = { rating ->
                        selectedMoodRating = rating
                    }
                )
            }
            item {
                MoodAndSymptomSquareView(
                    selectedMoods = selectedMoods,
                    selectedSymptoms = selectedSymptoms,
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
                        if(selectedMoodRating == 0 && selectedSymptoms.isEmpty() && selectedMoods.isEmpty()){
                            addNewUserViewModel.deleteDateDocument(selectedDay)
                        } else {
                            addNewUserViewModel.saveMoodToFirestore(selectedDay, selectedMoods, selectedSymptoms, selectedMoodRating)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Save Mood and Symptoms", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun CalendarSlide(
    onDaySelected: (LocalDate) -> Unit,
    navController: NavHostController,
    addNewUserViewModel: AddNewUserViewModel
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

        CustomTopAppBar(titleText = getWeekPageTitle(visibleWeek), onSettingsClicked = {
            navController.navigate("settings") // Navigate to settings screen
        },)
        WeekCalendar(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            state = state,
            dayContent = { day ->
                Day(day.date, isSelected = selection == day.date, addNewUserViewModel) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                        onDaySelected(clicked) // Invoke the callback with the selected date
                    }
                }
            },
        )
    }
}

suspend fun hasSavedDataForDate(viewModelScope: CoroutineScope, date: LocalDate): Boolean {
    val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
    val userId = currentUser.uid
    val db = FirebaseFirestore.getInstance()

    val datesCollectionRef = db.collection("Users").document(userId)
        .collection("Dates")

    return try {
        // Launching the operation on the ViewModelScope
        viewModelScope.async {
            val querySnapshot = datesCollectionRef.get().await()
            val dates = querySnapshot.documents.mapNotNull { document ->
                try {
                    LocalDate.parse(document.id)
                } catch (e: DateTimeParseException) {
                    null
                }
            }
            dates.contains(date)
        }.await()
    } catch (e: Exception) {
        Log.e(TAG, "Error checking for saved data on date: $date", e)
        false
    }
}


@Composable
private fun Day(date: LocalDate, isSelected: Boolean, addNewUserViewModel: AddNewUserViewModel, onClick: (LocalDate) -> Unit) {
    val today = LocalDate.now()
    val textStyle = MaterialTheme.typography.bodyLarge
    val datesWithData by addNewUserViewModel.datesWithData.collectAsState()

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
            Box(
                modifier = Modifier
                    .size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                if (date == today) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
                Text(
                    text = date.dayOfMonth.toString(),
                    style = textStyle,
                    color = when {
                        date == today -> MaterialTheme.colorScheme.onPrimary
                        isSelected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onBackground
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (date in datesWithData) {
            Box(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .size(4.dp)
                    .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                    .align(Alignment.BottomCenter)
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}


@Composable
fun CustomTopAppBar(
    titleText: String,
    onSettingsClicked: () -> Unit,
    addNewUserViewModel: AddNewUserViewModel = hiltViewModel()
) {
    var avatarIndex by remember { mutableStateOf<String?>(null) }
    var backgroundColorIndex by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(key1 = true) {
        val avatarImageString = addNewUserViewModel.fetchAvatarImageString() // This function now returns a String?
        val backgroundColorString = addNewUserViewModel.fetchAvatarBackgroundString() // This function now returns a String?

        avatarIndex = avatarImageString
        backgroundColorIndex = backgroundColorString
    }
    TopAppBar(
        elevation = 0.dp,
        title = { Text(text = titleText, color = MaterialTheme.colorScheme.onBackground) },
        backgroundColor = MaterialTheme.colorScheme.background,
        actions = {
            IconButton(onClick = onSettingsClicked) {
                DisplaySavedAvatarAndColor(avatarIndex,backgroundColorIndex,500)
            }
        }
    )
}

