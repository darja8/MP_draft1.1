package com.example.mp_draft10.pixabayAPI.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.firebase.auth.util.Resource
import com.example.mp_draft10.pixabayAPI.remote.PixabayAPI
import com.example.mp_draft10.pixabayAPI.remote.responses.ImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class ImageViewModel @Inject constructor(
    private val pixabayAPI: PixabayAPI
) : ViewModel() {

    private val _images = MutableLiveData<Resource<ImageResponse>>()
    val images: LiveData<Resource<ImageResponse>> = _images

    private val _imageBitmap = MutableLiveData<Resource<Bitmap>>()
    val imageBitmap: LiveData<Resource<Bitmap>> = _imageBitmap

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
    fun fetchImageFromUrl(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageBitmap.postValue(Resource.Loading())
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                connection.disconnect()

                if (bitmap != null) {
                    _imageBitmap.postValue(Resource.Success(bitmap))
                } else {
                    _imageBitmap.postValue(Resource.Error("Failed to decode bitmap"))
                }
            } catch (e: Exception) {
                _imageBitmap.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

}
