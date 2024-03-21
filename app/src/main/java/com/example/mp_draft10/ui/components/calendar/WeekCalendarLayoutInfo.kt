package com.example.mp_draft10.ui.components.calendar

import androidx.compose.foundation.lazy.LazyListLayoutInfo

class WeekCalendarLayoutInfo(
    info: LazyListLayoutInfo,
    private val getIndexData: (Int) -> Week,
) : LazyListLayoutInfo by info {
}

