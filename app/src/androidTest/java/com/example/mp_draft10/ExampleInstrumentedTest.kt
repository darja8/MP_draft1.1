package com.example.mp_draft10

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mp_draft10.database.AddNewUserViewModel
import com.example.mp_draft10.database.FirebaseUserRepository
import com.example.mp_draft10.database.PostViewModel
import com.example.mp_draft10.ui.components.MoodAndSymptomSquareView
import com.example.mp_draft10.ui.components.MoodRatingSquareView
import com.example.mp_draft10.ui.screens.PostDetailScreen
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Executors
import com.google.firebase.firestore.util.Util.voidErrorTransformer
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

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
@RunWith(AndroidJUnit4::class)
class PostDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var postViewModel: PostViewModel

    @Before
    fun setup() {
        // Mock the PostViewModel or use a fake if necessary
//        postViewModel = Mockito.mock(PostViewModel::class.java)
        postViewModel = mockk(relaxed = true)
        // Prepare your ViewModel with necessary data/state
    }

    @Test
    fun addingCommentDisplaysItOnScreen() {
        val commentText = "This is a new comment"

        composeTestRule.setContent {
            // Inject your mocked or fake ViewModel here
            PostDetailScreen(postId = "testPostId", postViewModel = postViewModel)
        }

        // Perform text input to simulate user entering a comment
        composeTestRule.onNodeWithText("Write a comment...").performTextInput(commentText)

        // Perform click action to simulate user sending the comment
        composeTestRule.onNodeWithText("Send Comment").performClick()

        // Delay to wait for the comment to be processed and displayed
        // Note: In real tests, use IdlingResource or similar mechanisms instead of Thread.sleep
        Thread.sleep(1000) // Example delay, adjust based on your needs

        // Assert that the comment text is now visible on the screen
        composeTestRule.onNode(hasText(commentText)).assertExists()
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

    @Test
    fun testingDatabaseAddUser() {

        // Tip 4
        val taskCompletionSource = TaskCompletionSource<Void>()

        val mockdb: FirebaseFirestore = mockk {
            every {
                collection(path)
                    .document(document.get("Id").toString())
                    .set(document)
                // Tip 4
            } returns taskCompletionSource.task.continueWith(Executors.DIRECT_EXECUTOR, voidErrorTransformer())
        }

        val firebaseCrud = spyk(AddNewUserViewModel(mockdb), recordPrivateCalls = true)

        runBlocking {
            firebaseCrud.addUserDetails(username, userEmail, userType)

            coVerify(exactly = 1) {
                firebaseCrud.addUserDetails(username, userEmail, userType)
            }
            confirmVerified(firebaseCrud)

            verify(exactly = 1) {
                mockdb
                    .collection(path)
                    .document(document.get("Id").toString())
                    .set(document)
            }
            confirmVerified(mockdb)

        }

        fun now(): Instant = Instant.now()
        //  Don't forget to unmock.
        unmockkStatic(::now)
    }
}