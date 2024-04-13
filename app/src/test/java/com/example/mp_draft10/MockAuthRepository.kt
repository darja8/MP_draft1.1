package com.example.mp_draft10

import com.example.mp_draft10.firebase.auth.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

interface MockAuthRepository {
    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
}

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    @Mock
    private lateinit var mockAuthRepository: MockAuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `register user success`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password123"
        val mockAuthResult: AuthResult = Mockito.mock(AuthResult::class.java) // Mocking AuthResult assuming it's an interface
        val expected: Flow<Resource<AuthResult>> = flow {
            emit(Resource.Success(mockAuthResult))
        }

        Mockito.`when`(mockAuthRepository.registerUser(email, password)).thenReturn(expected)

        val flow = mockAuthRepository.registerUser(email, password)
        flow.collect { result ->
            assert(result is Resource.Success)
            assertEquals(mockAuthResult, (result as Resource.Success<AuthResult>).data)
        }
    }

    @Test
    fun `register user failure`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password123"
        val expected: Flow<Resource<AuthResult>> = flow {
            emit(Resource.Error("Registration failed"))
        }

        Mockito.`when`(mockAuthRepository.registerUser(email, password)).thenReturn(expected)

        val flow = mockAuthRepository.registerUser(email, password)
        flow.collect { result ->
            assert(result is Resource.Error)
            assertEquals("Registration failed", result.message)
        }
    }

    @Test
    fun `login user success`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password123"
        val mockAuthResult: AuthResult = Mockito.mock(AuthResult::class.java) // Mocking AuthResult assuming it's an interface
        val expected: Flow<Resource<AuthResult>> = flow {
            emit(Resource.Success(mockAuthResult))
        }

        Mockito.`when`(mockAuthRepository.loginUser(email, password)).thenReturn(expected)

        val flow = mockAuthRepository.loginUser(email, password)
        flow.collect { result ->
            assert(result is Resource.Success)
            assertEquals(mockAuthResult, (result as Resource.Success<AuthResult>).data)
        }
    }

    @Test
    fun `login user failure`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password123"
        val expected: Flow<Resource<AuthResult>> = flow {
            emit(Resource.Error("Registration failed"))
        }

        Mockito.`when`(mockAuthRepository.loginUser(email, password)).thenReturn(expected)

        val flow = mockAuthRepository.loginUser(email, password)
        flow.collect { result ->
            assert(result is Resource.Error)
            assertEquals("Registration failed", result.message)
        }
    }
}

class DatabaseTest{

    @Mock
    private lateinit var mockViewModel: MockAddNewUserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun `add new user`(){

//        mockViewModel.addUserDetails()
    }

}