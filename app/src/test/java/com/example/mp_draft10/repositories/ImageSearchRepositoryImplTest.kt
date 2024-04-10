package com.example.mp_draft10.repositories

import com.example.mp_draft10.data.SamplePixabayProvider
import com.example.mp_draft10.pixabayAPI.remote.ApiService
import com.example.mp_draft10.pixabayAPI.repository.ImageSearchRepositoryImpl
import com.example.mp_draft10.pixabayAPI.repository.NetworkDataSource
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private lateinit var repository: ImageSearchRepositoryImpl
    private lateinit var dataSource: NetworkDataSource

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private var apiService: ApiService = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(mockWebServer.url("/"))
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

//    @Test
//    fun `successfully fetches album list returns success response`() = runTest {
//        // Setup MockWebServer response
//        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(SamplePixabayProvider.jsonResponse))
//
//        // Attempt to fetch data
//        val response = repository.fetchSearchData("apple")
//
//        // Assert that response is not null
//        assertThat(response).isNotNull()
//
//        // Deserialize the expected JSON response for comparison
//        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//        val jsonAdapter: JsonAdapter<PixabayResponse> = moshi.adapter(PixabayResponse::class.java)
//        val expectedResponse: PixabayResponse? = jsonAdapter.fromJson(SamplePixabayProvider.jsonResponse)
//
//        // Compare expected and actual results
//        assertThat(response?.hits).isEqualTo(expectedResponse?.hits)
//    }


    @Test(expected = IllegalStateException::class)
    fun `successfully fetches album list return empty list success response`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(SamplePixabayProvider.emptyListResponse)
        )

        val response = repository.fetchSearchData("apple")
        Truth.assertThat(response).isEqualTo(IllegalStateException("Empty product list"))
    }

    @Test(expected = retrofit2.HttpException::class)
    fun `error occured while fetching return error response`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(SamplePixabayProvider.failureResponse)
        )

        val response = repository.fetchSearchData("apple")
        Truth.assertThat(response).isInstanceOf(retrofit2.HttpException::class.java)
    }
}

