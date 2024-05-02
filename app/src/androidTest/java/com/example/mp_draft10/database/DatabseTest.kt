package com.example.mp_draft10.database

import android.app.Application
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import java.time.Instant

@ExperimentalCoroutinesApi
class AddNewUserViewModelTest {

    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirebaseFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var viewModel: AddNewUserViewModel

//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        // Ensure you are initializing your ViewModel correctly, considering its dependencies.
//
//        `when`(mockFirebaseAuth.currentUser).thenReturn(mockFirebaseUser)
//        `when`(mockFirebaseUser.uid).thenReturn("testUserId")
//
//        // Initialize your ViewModel here. Adjust according to your actual ViewModel's constructor.
//        viewModel = AddNewUserViewModel(mockApplication)
//        // Set up mock behavior and initialize viewModel if needed
//    }
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
    fun addUserDetailsTest() {
        val userName = "Test User"
        val userEmail = "test@example.com"
        val userType = "Test Type"

        val taskCompletionSource = TaskCompletionSource<Void>()



        val firebaseCrud = spyk(AddNewUserViewModel(mockApplication), recordPrivateCalls = true)

        runBlocking {
            firebaseCrud.addUserDetails(userName,userEmail,userType)

            coVerify(exactly = 1) {
                firebaseCrud.addUserDetails(userName,userEmail,userType)
            }
            confirmVerified(firebaseCrud)

            confirmVerified(mockFirebaseFirestore)
        }

        //  Don't forget to unmock.
        unmockkStatic(Instant::class)
    }
}
