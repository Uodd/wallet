package com.example.wallet

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.wallet.ui.theme.WalletTheme
import java.util.concurrent.Executor
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

private lateinit var executor: Executor
private lateinit var biometricPrompt: BiometricPrompt
private lateinit var promptInfo: BiometricPrompt.PromptInfo
@Composable
fun MainScreen(
    gotoLogin: (String)-> Unit,
    userid : String?,
    context: Context
){
    //Biometric(context)
    WalletTheme {
        Text("USER")

    }
}

