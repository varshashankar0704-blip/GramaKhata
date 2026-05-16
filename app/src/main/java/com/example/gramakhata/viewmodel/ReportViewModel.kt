package com.example.gramakhata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gramakhata.data.database.AppDatabase
import com.example.gramakhata.data.repository.ShopkeeperRepository
import com.example.gramakhata.data.repository.TransactionRepository
import com.example.gramakhata.utils.DateUtils

class ReportViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val transactions = TransactionRepository(db.transactionDao())
    private val shopkeepers = ShopkeeperRepository(db.shopkeeperDao())
    val shopkeeper = shopkeepers.observeShopkeeper()

    fun transactionsFor(day: Long) = DateUtils.dayBounds(day).let { transactions.observeForDate(it.first, it.second) }
    fun totalsFor(day: Long) = DateUtils.dayBounds(day).let { transactions.observeTotalsForDate(it.first, it.second) }
}
