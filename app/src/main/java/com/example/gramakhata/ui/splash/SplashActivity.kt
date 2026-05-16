package com.example.gramakhata.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.gramakhata.R
import com.example.gramakhata.ui.auth.LoginActivity
import com.example.gramakhata.ui.auth.RegistrationActivity
import com.example.gramakhata.ui.dashboard.DashboardActivity
import com.example.gramakhata.utils.Prefs

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = Prefs(this)
            val target = when {
                !prefs.isRegistered -> RegistrationActivity::class.java
                prefs.isLoggedIn -> DashboardActivity::class.java
                else -> LoginActivity::class.java
            }
            startActivity(Intent(this, target))
            finish()
        }, 2000)
    }
}
