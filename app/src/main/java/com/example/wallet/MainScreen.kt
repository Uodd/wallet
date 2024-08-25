package com.example.wallet

import android.content.Context
import android.view.View.inflate
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.wallet.ui.theme.WalletTheme
import java.util.concurrent.Executor
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.wallet.db.ActivityDb

import com.example.wallet.db.CardsDb
import com.example.wallet.db.UsersDb


@Composable
fun MainScreen(
    gotoLogin: ()-> Unit,
    userid : String,
    usersDb:UsersDb,
    activityDb: ActivityDb,
    cardsDB: CardsDb

){
    BioAuth("Test",userid,gotoLogin)

    val users=usersDb
    val currentUser= usersDb.getUsers(id=userid.toInt())[0]

    val activities=activityDb

    val cards = cardsDB
    Column {
        Text("Hola, ${currentUser.firstName} ${currentUser.lastName}!!")
        Text("Saldo : ${currentUser.saldo}") //Harcoded at UserData Class, only to show saved data TODO add saveSaldo(userId) to db handler

    }
}

