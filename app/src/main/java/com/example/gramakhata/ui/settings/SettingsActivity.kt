package com.example.gramakhata.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gramakhata.R
import com.example.gramakhata.data.database.entity.Shopkeeper
import com.example.gramakhata.ui.auth.LoginActivity
import com.example.gramakhata.ui.auth.RegistrationActivity
import com.example.gramakhata.utils.SecurityUtils
import com.example.gramakhata.viewmodel.SettingsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {
    private val vm: SettingsViewModel by viewModels()
    private var shopkeeper: Shopkeeper? = null
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_settings)
        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationIcon(android.R.drawable.ic_menu_revert)
        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
        vm.shopkeeper.observe(this) {
            shopkeeper = it ?: return@observe
            findViewById<TextInputEditText>(R.id.shopNameInput).setText(it.shopName)
            findViewById<TextInputEditText>(R.id.ownerNameInput).setText(it.ownerName)
        }
        findViewById<android.widget.Button>(R.id.saveShopButton).setOnClickListener { saveShop() }
        findViewById<android.widget.Button>(R.id.changePinButton).setOnClickListener { changePin() }
        findViewById<android.widget.Button>(R.id.logoutButton).setOnClickListener { logout() }
        findViewById<android.widget.Button>(R.id.clearButton).setOnClickListener { clearAll() }
    }
    private fun saveShop() {
        val s = shopkeeper ?: return
        val shop = findViewById<TextInputEditText>(R.id.shopNameInput).text.toString().trim()
        val owner = findViewById<TextInputEditText>(R.id.ownerNameInput).text.toString().trim()
        if (shop.isBlank() || owner.isBlank()) { Toast.makeText(this, "Shop and owner name are required", Toast.LENGTH_SHORT).show(); return }
        vm.saveShop(s.copy(shopName = shop, ownerName = owner)) { Toast.makeText(this, "Shop details saved", Toast.LENGTH_SHORT).show() }
    }
    private fun changePin() {
        val current = findViewById<TextInputEditText>(R.id.currentPinInput).text.toString()
        val next = findViewById<TextInputEditText>(R.id.newPinInput).text.toString()
        if (!SecurityUtils.isPin(current) || !SecurityUtils.isPin(next)) { Toast.makeText(this, "PIN must be 4 digits", Toast.LENGTH_SHORT).show(); return }
        vm.changePin(current, next) { Toast.makeText(this, if (it) "PIN changed" else "Current PIN is wrong", Toast.LENGTH_SHORT).show() }
    }
    private fun clearAll() {
        val input = EditText(this).apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD; hint = "4-digit PIN" }
        AlertDialog.Builder(this).setTitle("Clear all data?").setMessage("This deletes shop, customers, and transactions.").setView(input).setPositiveButton("Clear") { _, _ ->
            vm.clearAll(input.text.toString()) { ok ->
                if (ok) { startActivity(Intent(this, RegistrationActivity::class.java)); finishAffinity() }
                else Toast.makeText(this, "Wrong PIN", Toast.LENGTH_SHORT).show()
            }
        }.setNegativeButton("Cancel", null).show()
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Logout?")
            .setMessage("You will need to enter your mobile number and PIN again.")
            .setPositiveButton("Logout") { _, _ ->
                vm.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
