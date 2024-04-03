package com.example.mp_draft10.database

import com.google.firebase.database.PropertyName

data class User(
    @get:PropertyName("username")
    @set:PropertyName("username")
    var username: String = "",

    @get:PropertyName("userEmail")
    @set:PropertyName("userEmail")
    var userEmail: String = "",

    @get:PropertyName("userType")
    @set:PropertyName("userType")
    var userType: String = ""
)
{ constructor() : this("", "", "")}