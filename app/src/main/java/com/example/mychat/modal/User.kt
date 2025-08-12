package com.example.mychat.modal

data class User(
    val uid : String = "",
    val email : String = "",
    val name  : String  = "",
    val profileImage : String  = "",
    val createdAt : Long =0L
)
