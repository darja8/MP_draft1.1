package com.pixabay.imagesearch.domain

import com.example.mp_draft10.data.SamplePixabayProvider
import com.example.mp_draft10.pixabayAPI.repository.ImageSearchRepositoryImpl
import com.example.mp_draft10.usecases.ImageSearchUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class ImageSearchUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private val repository: ImageSearchRepositoryImpl = mock()
    private lateinit var useCase: ImageSearchUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = ImageSearchUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun fetchSearchDataNoImageList() = runTest {
        whenever(repository.fetchSearchData(any())).doSuspendableAnswer { SamplePixabayProvider.returnMappedResponse() }

        val useCase = ImageSearchUseCase(repository)
        val actual = useCase.execute("apple").first()
        advanceUntilIdle()
        val expected = SamplePixabayProvider.returnPixabayResponseFlow().first()
        assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun fetchSearchDataException() = runTest {

        lenient().`when`(repository.fetchSearchData(any()))
            .doThrow(IllegalStateException("Empty product list"))

        try {
            useCase.execute("apple")
        } catch (e: java.lang.IllegalStateException) {
            verify(repository).fetchSearchData(any())
            assertEquals("Empty product list", e.message)
        }
    }
}