package com.example.mp_draft10

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mp_draft10.ui.components.MoodAndSymptomSquareView
import com.example.mp_draft10.ui.components.MoodRatingSquareView
import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class MoodRatingSquareViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun moodRatingSelectionTest() {

        var selectedMoodRating = 0

        composeTestRule.setContent {
            MoodRatingSquareView(
                selectedMoodRating = selectedMoodRating,
                onMoodSelected = { selectedMoodRating = it }
            )
        }

        composeTestRule
            .onNodeWithText("5")
            .performClick()

        assert(selectedMoodRating == 5)
    }

    @Test
    fun selectsNauseousSymptom_AddsToList() {
        var selectedSymptoms = emptyList<String>()

        val onSymptomsSelected: (List<String>) -> Unit = { selectedSymptoms = it }

        composeTestRule.setContent {
            MoodAndSymptomSquareView(
                selectedMoods = emptyList(),
                selectedSymptoms = selectedSymptoms,
                onMoodsSelected = {},
                onSymptomsSelected = onSymptomsSelected
            )
        }
        composeTestRule.onNodeWithText("Nausea").performClick()

        assert(selectedSymptoms.contains("Nausea"))
    }

    @Test
    fun testMultipleSelections_AddsToRespectiveLists() {
        var selectedMoods = emptyList<String>()
        var selectedSymptoms = emptyList<String>()

        val onMoodsSelected: (List<String>) -> Unit = { selectedMoods = it }
        val onSymptomsSelected: (List<String>) -> Unit = { selectedSymptoms = it }


        composeTestRule.setContent {
            MoodAndSymptomSquareView(
                selectedMoods = selectedMoods,
                selectedSymptoms = selectedSymptoms,
                onMoodsSelected = onMoodsSelected,
                onSymptomsSelected = onSymptomsSelected
            )
        }

        composeTestRule.onNodeWithText("Sad").performClick()
        composeTestRule.onNodeWithText("Happy").performClick()
        composeTestRule.onNodeWithText("Headache").performClick()

        composeTestRule.waitForIdle()

        assert(selectedMoods.contains("Sad"))
        assert(selectedMoods.contains("Happy"))
        assert(selectedSymptoms.contains("Headache"))
    }


}


class FirebaseCRUDTest(){
    val path = "Users"
    val documentId = "Q1ueIQq75OUJ9O59IdO6rBVhKBm1"
    val avatarBackgroundMap = mutableMapOf<String, String>(
        "avatarImage" to "1",
        "BackgroundColor" to "3"
    )
    val document = mutableMapOf<String, Any>(
        "userEmail" to "d@test.com",
        "username" to "giraffe",
        "avatar" to avatarBackgroundMap
    )
    val userEmail = "d@test.com"
    val username = "giraffe"
    val avatar = avatarBackgroundMap
    val userType = "moderator"


    inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
        val task: Task<T> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedT: T = mockk(relaxed = true)
        every { task.result } returns result
        return task
    }

}
//
//class PostViewModelTest(){
//    private lateinit var postViewModel: PostViewModel
//
//    @Before
//    fun setup(){
//        postViewModel =
//    }
//}