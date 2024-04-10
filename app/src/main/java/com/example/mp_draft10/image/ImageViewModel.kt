package com.example.mp_draft10.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.auth.util.Resource
import com.example.mp_draft10.remote.PixabayAPI
import com.example.mp_draft10.remote.responses.ImageResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageViewModel @Inject constructor(
    private val pixabayAPI: PixabayAPI
) : ViewModel() {

    private val _images = MutableLiveData<Resource<ImageResponse>>()
    val images: LiveData<Resource<ImageResponse>> = _images

    fun searchImages(query: String) {
        viewModelScope.launch {
            _images.postValue(Resource.Loading())
            try {
                val response = pixabayAPI.searchForImage(searchQuery = query)
                if (response.isSuccessful) {
                    _images.postValue(Resource.Success(response.body()!!))
                } else {
                    _images.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                _images.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }
}
