package com.example.wallet

import kotlinx.serialization.Serializable

@Serializable
data class UserData(val id:Int,val firstName:String,val lastName:String)

@Serializable
object LogInView
