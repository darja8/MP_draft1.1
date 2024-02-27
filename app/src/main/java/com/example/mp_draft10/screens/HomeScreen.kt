package com.example.mp_draft10.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import java.time.temporal.ChronoUnit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp
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
        Text(
            text = "Today View",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
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

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    TodayScreen()
//    HorizontalCalendar()
}