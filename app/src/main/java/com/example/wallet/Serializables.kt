package com.example.wallet

import kotlinx.serialization.Serializable
import javax.net.ssl.SSLEngineResult.Status

@Serializable
data class UserData(val id:Int=-1,val firstName:String,val lastName:String,val time:String)

@Serializable
data class ActivityData(val id:Int,val userid:Int,val status: Int,val time:String)

@Serializable
object LogInView
