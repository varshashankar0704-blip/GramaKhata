package com.example.gramakhata.utils

import java.security.MessageDigest

object SecurityUtils {
    fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }

    fun isMobile(value: String) = value.matches(Regex("\\d{10}"))
    fun isPin(value: String) = value.matches(Regex("\\d{4}"))
}
