package com.example.gramakhata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.gramakhata.data.database.AppDatabase
import com.example.gramakhata.data.database.dao.CustomerBalance
import com.example.gramakhata.data.repository.CustomerRepository
import com.example.gramakhata.data.repository.ShopkeeperRepository
import com.example.gramakhata.data.repository.TransactionRepository
import com.example.gramakhata.utils.DateUtils

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val customers = CustomerRepository(db.customerDao())
    private val transactions = TransactionRepository(db.transactionDao())
    private val shopkeepers = ShopkeeperRepository(db.shopkeeperDao())
    val shopkeeper = shopkeepers.observeShopkeeper()
    val query = MutableLiveData("")
    val customerBalances = MediatorLiveData<List<CustomerBalance>>()
    private val today = DateUtils.dayBounds(System.currentTimeMillis())
    val todayCollection = transactions.observePaymentTotalForDate(today.first, today.second)

    init {
        var all = emptyList<CustomerBalance>()
        fun publish() {
            val q = query.value.orEmpty().trim()
            customerBalances.value = if (q.isBlank()) all else all.filter { it.name.contains(q, true) }
        }
        customerBalances.addSource(customers.observeCustomerBalances()) { all = it; publish() }
        customerBalances.addSource(query) { publish() }
    }
}
