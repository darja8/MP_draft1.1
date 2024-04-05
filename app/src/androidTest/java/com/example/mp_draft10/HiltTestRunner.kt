//package com.example.mp_draft10
//
//import android.app.Application
//import android.content.Context
//import androidx.test.runner.AndroidJUnitRunner
//import com.example.mp_draft10.database.PostViewModel
//import com.example.mp_draft10.ui.screens.Comment
//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.TaskCompletionSource
//import com.google.firebase.Timestamp
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.util.Executors
//import com.google.firebase.firestore.util.Util.voidErrorTransformer
//import dagger.hilt.android.testing.HiltAndroidTest
//import dagger.hilt.android.testing.HiltTestApplication
//import io.mockk.coVerify
//import io.mockk.confirmVerified
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.spyk
//import io.mockk.verify
//import kotlinx.coroutines.runBlocking
//import org.junit.Test
//
//class HiltTestRunner : AndroidJUnitRunner() {
//    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
//        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
//    }
//}
//
//@HiltAndroidTest
//class PostViewModelTest {
//
//    val path = "posts"
//    val documentId = "2507"
//    val postId = "1"
//    val postText = "Test"
//
//    val newComment = Comment("test","12345", 1234,"1")
//
//    inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
//        val task: Task<T> = mockk(relaxed = true)
//        every { task.isComplete } returns true
//        every { task.exception } returns exception
//        every { task.isCanceled } returns false
//        val relaxedT: T = mockk(relaxed = true)
//        every { task.result } returns result
//        return task
//    }
//
//    @Test
//    fun testAddPost() {
//
//        // Tip 4
//        val taskCompletionSource = TaskCompletionSource<Void>()
//        lateinit var mockApplication: Application
//
//        val mockdb: FirebaseFirestore = mockk {
//            every {
//                collection(path)
//                    .document(document(postId).toString())
//                    .set(document(postId))
//            } returns taskCompletionSource.task.continueWith(Executors.DIRECT_EXECUTOR, voidErrorTransformer())
//        }
//
//        val firebaseCrud = spyk(PostViewModel(mockApplication,mockdb), recordPrivateCalls = true)
//
//        runBlocking {
//            firebaseCrud.addCommentToPost(postId,newComment)
//
//            coVerify(exactly = 1) {
//                firebaseCrud.addCommentToPost(postId,newComment)
//            }
//            confirmVerified(firebaseCrud)
//
//            verify(exactly = 1) {
//                mockdb.collection(path)
//                    .document(documentId)
//                    .set(any()) // Match this with how it's actually used
//            }
//            confirmVerified(mockdb)
//
//            verify(exactly = 0) {
//                Timestamp.now()
//            }
//        }
//    }
//}