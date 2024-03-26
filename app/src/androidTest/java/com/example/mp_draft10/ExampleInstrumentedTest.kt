
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mp_draft10.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import drawable.TodayScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TodayScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            val navController = rememberNavController()
            TodayScreen(navController = navController)
        }
    }

    @Test
    fun saveMoodButton_click_performsSaveAction() {
        // Attempt to click the "Save Mood and Symptoms" button.
        composeTestRule.onNodeWithText("Save Mood and Symptoms").performClick()

        // Add assertions here to verify the outcome.
        // This could involve verifying a change in the UI,
        // or checking that a certain function was called in the ViewModel.
    }
}