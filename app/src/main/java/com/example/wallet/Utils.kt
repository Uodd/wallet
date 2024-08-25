package com.example.wallet

import java.text.SimpleDateFormat
import java.util.Date

val datetimeFormat = SimpleDateFormat("YYYY-MM-DD hh:mm:ss")
fun getTimeString():String{
    return datetimeFormat.format(Date())
}