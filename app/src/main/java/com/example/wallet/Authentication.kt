package com.example.wallet;

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.delay
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

private lateinit var biometricPrompt: BiometricPrompt
private lateinit var promptInfo: BiometricPrompt.PromptInfo

val TAG = "AUTHENTICATION"

@Composable
fun AuthenticateMain(
    successCallback: () -> Unit,
    exitCallback: () -> Unit,
    keyalias: String = "dummy" //maybe this variable could be used to add another encryption layer?
) {
    Log.v(TAG, "Started Encryption")
    val cipher = prepareEncrypt(keyalias)
    val activity = LocalContext.current as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    LaunchedEffect(Unit) {
        delay(500)
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authenticate to Start")
        .setSubtitle("This App secures (?) your data")
        .setNegativeButtonText("Exit")
        .build()

    biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                exitCallback()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                successCallback()
                Log.v(TAG, "Encrypted information")
            }

            override fun onAuthenticationFailed() {
                exitCallback()
            }
        })
}

@Composable
fun BioEncryptData(
    doneCallback:(ByteArray?) -> Unit,
    exitCallback: () -> Unit,
    data: ByteArray ,
    keyalias: String = "dummy",
    decrypt: Boolean =false
) {
    Log.v(TAG, "Started Encryption")
    val cipher = if(!decrypt) prepareEncrypt(keyalias) else prepareDecrypt(keyalias)
    val activity = LocalContext.current as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    LaunchedEffect(Unit) {
        delay(500)
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometrics allow access to secret key")
        .setSubtitle("Authenticate")
        .setNegativeButtonText("Exit")
        .build()

    biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                exitCallback()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                //val encryptedData = mutableListOf<ByteArray>()

                //it is not possible for now to encrypt multiple times with one prompt. Superchunk blends all data in one piece to chopp when decrypted
                val encryptedD = result.cryptoObject?.cipher?.doFinal(data)
                Log.v(TAG,"DATA: $data")
                Log.v(TAG, "${if(decrypt) "De" else "En"}crypted information $encryptedD")
                doneCallback(encryptedD)

            }
            override fun onAuthenticationFailed() {
                exitCallback()
            }
        })
}

fun prepareEncrypt(keyalias: String): Cipher {
    val cipher = getCipher()
    val secretKey = getSecretKey(keyalias)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    return cipher
}
fun prepareDecrypt(keyalias: String):Cipher{
    val cipher = getCipher()
    val secretKey = getSecretKey(keyalias)
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return cipher
}

private fun getCipher(): Cipher {
    return Cipher.getInstance("AES/CBC/PKCS7PADDING")
}

private fun getSecretKey(alias: String): SecretKey {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    val key = keyStore.getKey(alias, null)
    if (key == null) {
        generateSecretKey(alias)
        return getSecretKey(alias)

    }
    return key as SecretKey
}

private fun generateSecretKey(alias: String) {
    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .setUserAuthenticationRequired(true)
        // Invalidate the keys if the user has registered a new biometric
        // credential, such as a new fingerprint. Can call this method only
        // on Android 7.0 (API level 24) or higher. The variable
        // "invalidatedByBiometricEnrollment" is true by default.
        .setInvalidatedByBiometricEnrollment(true)
        .build()

    val kg = KeyGenerator.getInstance("AES", "AndroidKeyStore")
    kg.init(parameterSpec)
    kg.generateKey()
}
/////////////////////////////////////////////////////////////////// WORKARROUND NOT WORKING
//private fun reLoadCipher(cipher:Cipher,alias: String){
//    val r = SecureRandom()
//    val ivBytes = ByteArray(16)
//    r.nextBytes(ivBytes)
//    cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias),IvParameterSpec(ivBytes))
//}

