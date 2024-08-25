package com.example.wallet;

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.delay
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private lateinit var biometricPrompt: BiometricPrompt
private lateinit var promptInfo: BiometricPrompt.PromptInfo

@Composable
fun BioAuth(data:String,alias:String, exitCallback:()->Unit){
    Log.v("INFO","Started Encryption")

    val cipher = getCipher()
    val secretKey =getSecretKey(alias)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val activity = LocalContext.current as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    biometricPrompt = BiometricPrompt(activity, executor,
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int,
                                           errString: CharSequence) {
            exitCallback()
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

            val encryptedInfo: ByteArray? = result.cryptoObject?.cipher?.doFinal(
                data.toByteArray()
            )

            Log.v("MY_APP_TAG", "Encrypted information: " +
                    encryptedInfo.contentToString()
            )
        }
        override fun onAuthenticationFailed() {
            exitCallback()
        }
    })

    promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometrics allow access to secret key")
        .setSubtitle("Authenticate")
        .setNegativeButtonText("Exit")
        .build()

    LaunchedEffect(Unit){
        delay(500)
        biometricPrompt.authenticate(promptInfo,BiometricPrompt.CryptoObject(cipher))
    }



}

    private fun getCipher():Cipher{
        return Cipher.getInstance("AES/CBC/PKCS7PADDING")
    }
    private fun getSecretKey(alias:String):SecretKey{
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val key = keyStore.getKey(alias,null)
        if(key==null){
            generateSecretKey(alias)
            return getSecretKey(alias)

        }
        return  key as SecretKey
    }
    private fun generateSecretKey(alias:String){
        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias
            ,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            // Invalidate the keys if the user has registered a new biometric
            // credential, such as a new fingerprint. Can call this method only
            // on Android 7.0 (API level 24) or higher. The variable
            // "invalidatedByBiometricEnrollment" is true by default.
            .setInvalidatedByBiometricEnrollment(true)
            .build()

        val kg = KeyGenerator.getInstance("AES","AndroidKeyStore")
        kg.init(parameterSpec)
        kg.generateKey()
    }


