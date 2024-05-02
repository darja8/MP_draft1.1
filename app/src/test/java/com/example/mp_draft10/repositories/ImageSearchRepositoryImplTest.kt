package com.example.mp_draft10.repositories

import com.example.mp_draft10.data.SamplePixabayProvider
import com.example.mp_draft10.pixabayAPI.remote.ApiService
import com.example.mp_draft10.pixabayAPI.repository.ImageSearchRepositoryImpl
import com.example.mp_draft10.pixabayAPI.repository.NetworkDataSource
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class ImageSearchRepositoryImplTest {

    private val mockWebServer = MockWebServer()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private lateinit var repository: ImageSearchRepositoryImpl
    private lateinit var dataSource: NetworkDataSource


    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private var apiService: ApiService = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(mockWebServer.url("/").toString())
        .client(client)
        .build()
        .create(ApiService::class.java)

    @Before
    fun setUp() {
        dataSource = NetworkDataSource(apiService)
        repository = ImageSearchRepositoryImpl(dataSource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test(expected = IllegalStateException::class)
    fun successfullyFetchAlbum() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(SamplePixabayProvider.emptyListResponse)
        )

        val response = repository.fetchSearchData("apple")
        Truth.assertThat(response).isEqualTo(IllegalStateException("Empty product list"))
    }

    @Test(expected = retrofit2.HttpException::class)
    fun errorOccurredWhileFetching() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(SamplePixabayProvider.failureResponse)
        )

        val response = repository.fetchSearchData("apple")
        Truth.assertThat(response).isInstanceOf(retrofit2.HttpException::class.java)
    }
}

