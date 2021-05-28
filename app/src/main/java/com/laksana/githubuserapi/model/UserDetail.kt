package com.laksana.githubuserapi.model

data class UserDetail (
    var username: String,
    var location: String,
    var repository: String,
    var followers: String,
    var following: String,
    var avatar: String
)