package com.example.wallet;

import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/*
 * Generate a new EC key pair entry in the Android Keystore by
 * using the KeyPairGenerator API. The private key can only be
 * used for signing or verification and only with SHA-256 or
 * SHA-512 as the message digest.
 */
class Authentication() {
    private val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        "mainKey"
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
    private val kg = KeyGenerator.getInstance("AES","AndroidKeyStore")

    init {
        kg.init(parameterSpec)
        val cipher = getCipher()
        val secretKey =getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE,secretKey)
    }

    private fun getCipher():Cipher{
        return Cipher.getInstance("AES/CBC/PKCS5PADDING")
    }
    private fun getSecretKey():SecretKey{
        return kg.generateKey()
    }

}
/*
generateSecretKey(KeyGenParameterSpec.Builder(
KEY_NAME,
KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
.setBlockModes(KeyProperties.BLOCK_MODE_CBC)
.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
.setUserAuthenticationRequired(true)
// Invalidate the keys if the user has registered a new biometric
// credential, such as a new fingerprint. Can call this method only
// on Android 7.0 (API level 24) or higher. The variable
// "invalidatedByBiometricEnrollment" is true by default.
.setInvalidatedByBiometricEnrollment(true)
.build())
