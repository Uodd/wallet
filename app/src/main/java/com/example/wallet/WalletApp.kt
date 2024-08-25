package com.example.wallet

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wallet.db.ActivityDb
import com.example.wallet.db.CardsDb
import com.example.wallet.db.UsersDb
import com.example.wallet.ui.theme.WalletTheme

@Composable
fun WalletApp(context: Context){
    Log.v("INFO", "Starting WalletApp")

    val navController = rememberNavController()

    WalletTheme {

        var entryPoint="login"
        val activityDb = ActivityDb(context)
        val usersDb = UsersDb(context)
        val lastActivity= activityDb.getLast()
        if(lastActivity != null){ //TODO: add time check
            entryPoint = "home/{lastActivity}"
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
        ) { innerPadding ->
            NavHost(navController , startDestination = entryPoint, modifier = Modifier.padding(innerPadding)) {
                composable(route = "home/{userId}",
                    arguments = listOf(navArgument("userId"){type= NavType.StringType}))
                {backStackEntry->
                    backStackEntry.arguments!!.getString("userId")?.let {
                        MainScreen(gotoLogin = { navController.navigate("login")},
                            it,
                            usersDb,
                            activityDb,
                            CardsDb(context)
                        )
                    }
                }
                composable(route = "login") {
                    LogInScreen(
                        gotoMain = {
                        userId:String ->
                        navController.navigate("home/$userId")
                                   },
                        gotoLogin = {navController.navigate("login")},
                        usersDb = usersDb
                    )
                }
            }
        }
    }
}