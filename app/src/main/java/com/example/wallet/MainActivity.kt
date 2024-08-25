package com.example.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity


class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.deleteDatabase("ActivityData.db") //Debugging databases... deletes db file
        //this.deleteDatabase("UsersData.db")
        //this.deleteDatabase("creditCards.db")
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