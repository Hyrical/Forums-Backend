package org.hyrical.utils

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Encryptions {

    fun encrypt(value: String): String {
        val key = "jXn2r4u7x!A%D*G-KaPdSgVkYp3s6v8y"
        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(value: String): String {
        val key = "jXn2r4u7x!A%D*G-KaPdSgVkYp3s6v8y"
        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decoded = Base64.getDecoder().decode(value)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted)
    }
}