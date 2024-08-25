package com.example.wallet

import kotlinx.serialization.Serializable
import org.w3c.dom.Text
import javax.net.ssl.SSLEngineResult.Status

@Serializable
data class UserData(val id:Int=-1,val firstName:String,val lastName:String,val saldo:Float=0.toFloat(), val time:String)

@Serializable
data class ActivityData(val id:Int=-1,val userid:Int,val status: Int,val time:String)

@Serializable
data class CardData(val id:Int=-1,val number:String,val secure:String,val expire:String,val userid:Int, val public:String,val brand:String)