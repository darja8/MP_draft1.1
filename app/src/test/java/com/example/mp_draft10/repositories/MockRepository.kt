package com.example.mp_draft10.repositories

import android.app.Application
import com.example.mp_draft10.auth.util.Resource
import com.example.mp_draft10.database.AddNewUserViewModel
import com.example.mp_draft10.database.DatabaseRepository
import com.example.mp_draft10.remote.responses.ImageResponse

class MockRepository : DatabaseRepository {

//    private val user = MutableLiveData
    private val shouldReturnNetworkError = false

    override fun addUserDetails(
        addNewUserViewModel: AddNewUserViewModel,
        application: Application
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
            Resource.Error("Error", null)
        }else{
            Resource.Success(ImageResponse(listOf(), 0,0))
        }
    }


}