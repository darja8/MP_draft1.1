package com.example.mp_draft10

//import com.google.firebase.auth.AuthResult
import android.app.Application
import com.example.mp_draft10.database.PostViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class ViewModelTest{

    private lateinit var viewModel: PostViewModel

    @Before
    fun setup(){
        viewModel = PostViewModel(Application())
    }
}