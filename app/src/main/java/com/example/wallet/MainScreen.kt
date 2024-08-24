package com.example.wallet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wallet.ui.theme.WalletTheme

@Composable
fun MainScreen(
    onGOTOCards: ()-> Unit,
   // user : UserData
){
    WalletTheme {
        Text("USER")

    }

}