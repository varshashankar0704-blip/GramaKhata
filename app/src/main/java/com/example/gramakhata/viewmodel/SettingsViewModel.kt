package com.example.gramakhata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakhata.data.database.AppDatabase
import com.example.gramakhata.data.database.entity.Shopkeeper
import com.example.gramakhata.data.repository.CustomerRepository
import com.example.gramakhata.data.repository.ShopkeeperRepository
import com.example.gramakhata.data.repository.TransactionRepository
import com.example.gramakhata.utils.Prefs
import com.example.gramakhata.utils.SecurityUtils
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val shopkeepers = ShopkeeperRepository(db.shopkeeperDao())
    private val customers = CustomerRepository(db.customerDao())
    private val transactions = TransactionRepository(db.transactionDao())
    private val prefs = Prefs(app)
    val shopkeeper = shopkeepers.observeShopkeeper()

    fun saveShop(shopkeeper: Shopkeeper, done: () -> Unit) = viewModelScope.launch { shopkeepers.update(shopkeeper); done() }

    fun changePin(current: String, next: String, result: (Boolean) -> Unit) = viewModelScope.launch {
        val shop = shopkeepers.getShopkeeper()
        val ok = shop != null && shop.pinHash == SecurityUtils.sha256(current)
        if (ok) shopkeepers.update(shop!!.copy(pinHash = SecurityUtils.sha256(next)))
        result(ok)
    }

    fun clearAll(pin: String, done: (Boolean) -> Unit) = viewModelScope.launch {
        val shop = shopkeepers.getShopkeeper()
        val ok = shop != null && shop.pinHash == SecurityUtils.sha256(pin)
        if (ok) {
            transactions.clear()
            customers.clear()
            shopkeepers.clear()
            prefs.clear()
        }
        done(ok)
    }

    fun logout() {
        prefs.isLoggedIn = false
    }
}
