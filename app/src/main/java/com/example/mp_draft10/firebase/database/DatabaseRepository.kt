package com.example.mp_draft10.firebase.database

import android.app.Application
import com.example.mp_draft10.firebase.auth.util.Resource
import com.example.mp_draft10.pixabayAPI.remote.responses.ImageResponse

interface DatabaseRepository {

    fun addUserDetails(addNewUserViewModel: AddNewUserViewModel, application: Application)

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}