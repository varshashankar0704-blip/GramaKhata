package com.example.gramakhata.utils

import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("grama_khata_prefs", Context.MODE_PRIVATE)

    var isRegistered: Boolean
        get() = prefs.getBoolean("isRegistered", false)
        set(value) = prefs.edit().putBoolean("isRegistered", value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean("isLoggedIn", false)
        set(value) = prefs.edit().putBoolean("isLoggedIn", value).apply()

    var mobileNumber: String
        get() = prefs.getString("mobileNumber", "") ?: ""
        set(value) = prefs.edit().putString("mobileNumber", value).apply()

    fun clear() = prefs.edit().clear().apply()
}
