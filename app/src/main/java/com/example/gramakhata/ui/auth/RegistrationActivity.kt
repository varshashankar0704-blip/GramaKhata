package com.example.gramakhata.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gramakhata.R
import com.example.gramakhata.ui.dashboard.DashboardActivity
import com.example.gramakhata.utils.SecurityUtils
import com.example.gramakhata.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistrationActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val shop = findViewById<TextInputEditText>(R.id.shopNameInput)
        val owner = findViewById<TextInputEditText>(R.id.ownerNameInput)
        val mobile = findViewById<TextInputEditText>(R.id.mobileInput)
        val pin = findViewById<TextInputEditText>(R.id.pinInput)
        val confirm = findViewById<TextInputEditText>(R.id.confirmPinInput)
        findViewById<android.widget.Button>(R.id.registerButton).setOnClickListener {
            clearErrors()
            val ok = required(R.id.shopNameLayout, shop, "Shop name is required") and
                required(R.id.ownerNameLayout, owner, "Owner name is required") and
                valid(R.id.mobileLayout, SecurityUtils.isMobile(mobile.text.toString()), "Mobile number must be 10 digits") and
                valid(R.id.pinLayout, SecurityUtils.isPin(pin.text.toString()), "PIN must be 4 digits") and
                valid(R.id.confirmPinLayout, confirm.text.toString() == pin.text.toString(), "PINs must match")
            if (ok) vm.register(shop.text.toString(), owner.text.toString(), mobile.text.toString(), pin.text.toString()) {
                startActivity(Intent(this, DashboardActivity::class.java)); finish()
            }
        }
    }
    private fun clearErrors() = listOf(R.id.shopNameLayout, R.id.ownerNameLayout, R.id.mobileLayout, R.id.pinLayout, R.id.confirmPinLayout).forEach { findViewById<TextInputLayout>(it).error = null }
    private fun required(id: Int, input: TextInputEditText, msg: String) = valid(id, input.text?.isNotBlank() == true, msg)
    private fun valid(id: Int, ok: Boolean, msg: String): Boolean { if (!ok) findViewById<TextInputLayout>(id).error = msg; return ok }
}
