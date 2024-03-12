package com.example.mp_draft10.database

import android.app.Application

interface DatabaseRepository {
    fun addUserDetails(addNewUserViewModel: AddNewUserViewModel, application: Application)
}