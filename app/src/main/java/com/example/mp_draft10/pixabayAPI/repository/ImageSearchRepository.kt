package com.example.mp_draft10.pixabayAPI.repository

import com.example.mp_draft10.data.entities.MappedImageItemModel

interface ImageSearchRepository {
    suspend fun fetchSearchData(query: String): List<MappedImageItemModel>
}