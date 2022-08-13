package org.hyrical.utils

import com.google.common.hash.Hashing

object HashUtils {
    fun hash(value: String): String {
        return Hashing.sha256().hashString(value, Charsets.UTF_8).toString()
    }
}