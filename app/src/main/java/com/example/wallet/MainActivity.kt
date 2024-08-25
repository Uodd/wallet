package com.example.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.deleteDatabase("ActivityData.db") //Debugging databases... deletes db file
        enableEdgeToEdge()
        setContent {
            WalletApp(this)
        }
    }
}



/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WalletTheme {
        Greeting("Android")
    }
}*/