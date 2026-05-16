package com.example.gramakhata.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gramakhata.R
import com.example.gramakhata.ui.dashboard.DashboardActivity
import com.example.gramakhata.utils.Prefs
import com.example.gramakhata.utils.SecurityUtils
import com.example.gramakhata.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mobile = findViewById<TextInputEditText>(R.id.mobileInput)
        val pin = findViewById<TextInputEditText>(R.id.pinInput)
        val error = findViewById<TextView>(R.id.errorText)
        mobile.setText(Prefs(this).mobileNumber)
        findViewById<android.widget.Button>(R.id.loginButton).setOnClickListener {
            error.text = ""
            if (!SecurityUtils.isMobile(mobile.text.toString()) || !SecurityUtils.isPin(pin.text.toString())) {
                error.text = "Enter a valid mobile number and 4-digit PIN."
                return@setOnClickListener
            }
            vm.login(mobile.text.toString(), pin.text.toString()) { ok ->
                if (ok) { startActivity(Intent(this, DashboardActivity::class.java)); finish() }
                else error.text = "Invalid mobile number or PIN. Please try again."
            }
        }
    }
}
