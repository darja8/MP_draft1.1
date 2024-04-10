package com.example.mp_draft10.pixabayAPI.repository

import com.example.mp_draft10.data.entities.PixabayResponse
import com.example.mp_draft10.pixabayAPI.remote.ApiService
import javax.inject.Inject

/*Network data source to fetch data from server using api service client*/
class NetworkDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchSearchData(query: String): PixabayResponse = apiService.getSearchResponse(query)
}