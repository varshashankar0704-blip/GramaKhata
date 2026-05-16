package com.example.gramakhata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakhata.data.database.AppDatabase
import com.example.gramakhata.data.database.entity.Shopkeeper
import com.example.gramakhata.data.repository.ShopkeeperRepository
import com.example.gramakhata.utils.Prefs
import com.example.gramakhata.utils.SecurityUtils
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ShopkeeperRepository(AppDatabase.get(app).shopkeeperDao())
    private val prefs = Prefs(app)

    fun register(shopName: String, owner: String, mobile: String, pin: String, done: () -> Unit) {
        viewModelScope.launch {
            repo.register(Shopkeeper(shopName = shopName, ownerName = owner, mobileNumber = mobile, pinHash = SecurityUtils.sha256(pin)))
            prefs.isRegistered = true
            prefs.isLoggedIn = true
            prefs.mobileNumber = mobile
            done()
        }
    }

    fun login(mobile: String, pin: String, result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val shopkeeper = repo.getShopkeeper()
            val ok = shopkeeper?.mobileNumber == mobile && shopkeeper.pinHash == SecurityUtils.sha256(pin)
            prefs.isLoggedIn = ok
            if (ok) prefs.mobileNumber = mobile
            result(ok)
        }
    }
}
