package com.example.mp_draft10.auth

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

@ExperimentalCoroutinesApi
class SignUpViewModelTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        // Initialize your mocked repository and ViewModel here
        mockRepository = mockk(relaxed = true)
        viewModel = SignUpViewModel(mockRepository)
    }

//    @Test
//    @ExperimentalTime
//    fun registerUserWithSuccess() = runTest {
//        // Arrange
//        coEvery { mockRepository.registerUser(any(), any()) } returns flowOf(Resource.Success())
//
//        // Act
//        viewModel.registerUser("test@example.com", "password123")
//
//        // Assert
//        viewModel.signUpState.test {
//            val emission = awaitItem()
//            assertTrue(emission.isSuccess)
//            assertEquals("Sign up success", emission.message)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
}
