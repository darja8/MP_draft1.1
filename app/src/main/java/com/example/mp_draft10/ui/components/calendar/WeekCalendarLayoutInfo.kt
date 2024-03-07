package com.example.mp_draft10.ui.components.calendar

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo

class WeekCalendarLayoutInfo(
    info: LazyListLayoutInfo,
    private val getIndexData: (Int) -> Week,
) : LazyListLayoutInfo by info {
    /**
     * The list of [WeekCalendarItemInfo] representing all the currently visible weeks.
     */
    val visibleWeeksInfo: List<WeekCalendarItemInfo>
        get() = visibleItemsInfo.map { info ->
            WeekCalendarItemInfo(info, getIndexData(info.index))
        }
}

/**
 * Contains useful information about an individual week on the calendar.
 *
 * @param week The week in the list.

 * @see WeekCalendarLayoutInfo
 * @see LazyListItemInfo
 */
class WeekCalendarItemInfo(info: LazyListItemInfo, val week: Week) :
    LazyListItemInfo by info
