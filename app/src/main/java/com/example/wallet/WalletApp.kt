package com.example.wallet

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wallet.ui.theme.WalletTheme

@Composable
fun WalletApp(){
    val navController = rememberNavController()

    WalletTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
        ) { innerPadding ->
            NavHost(navController , startDestination = "login", modifier = Modifier.padding(innerPadding)) {
                composable(route = "home"){
                    MainScreen(onGOTOCards = { navController.navigate("login")})
                }
                composable(route = "login") {
                    LogInScreen(onGOTOMain = {navController.navigate("home")})
                }
            }
        }
    }
}