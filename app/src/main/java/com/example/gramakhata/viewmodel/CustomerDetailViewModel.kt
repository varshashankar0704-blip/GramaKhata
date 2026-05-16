package com.example.gramakhata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakhata.data.database.AppDatabase
import com.example.gramakhata.data.database.entity.Customer
import com.example.gramakhata.data.database.entity.LedgerTransaction
import com.example.gramakhata.data.database.entity.TransactionType
import com.example.gramakhata.data.repository.CustomerRepository
import com.example.gramakhata.data.repository.ShopkeeperRepository
import com.example.gramakhata.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class CustomerDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val customers = CustomerRepository(db.customerDao())
    private val transactions = TransactionRepository(db.transactionDao())
    private val shopkeepers = ShopkeeperRepository(db.shopkeeperDao())
    val shopkeeper = shopkeepers.observeShopkeeper()

    fun observeCustomer(id: Int) = customers.observeCustomer(id)
    fun observeTransactions(id: Int) = transactions.observeForCustomer(id)
    fun observeRecent(id: Int) = transactions.observeRecentForCustomer(id)
    fun observeNetDue(id: Int) = transactions.observeNetDue(id)
    fun observeTotals(id: Int) = transactions.observeTotals(id)

    fun addTransaction(customerId: Int, type: TransactionType, amount: Double, note: String?, date: Long, done: () -> Unit = {}) {
        viewModelScope.launch {
            transactions.add(LedgerTransaction(customerId = customerId, type = type, amount = amount, note = note, createdAt = date))
            done()
        }
    }

    fun addCustomer(customer: Customer, allowDuplicate: Boolean, result: (Boolean, Boolean) -> Unit) {
        viewModelScope.launch {
            val duplicate = customers.phoneExists(customer.phoneNumber)
            if (duplicate && !allowDuplicate) result(false, true) else {
                customers.add(customer)
                result(true, false)
            }
        }
    }

    fun updateCustomer(customer: Customer, result: (Boolean, Boolean) -> Unit) {
        viewModelScope.launch {
            val duplicate = customers.phoneExists(customer.phoneNumber, customer.customerId)
            if (duplicate) result(false, true) else {
                customers.update(customer)
                result(true, false)
            }
        }
    }

    fun deleteCustomer(customer: Customer, done: () -> Unit) = viewModelScope.launch { customers.delete(customer); done() }
    fun deleteTransaction(transaction: LedgerTransaction) = viewModelScope.launch { transactions.delete(transaction) }
}
