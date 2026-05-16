package com.example.gramakhata.data.repository

import com.example.gramakhata.data.database.dao.TransactionDao
import com.example.gramakhata.data.database.entity.LedgerTransaction

class TransactionRepository(private val dao: TransactionDao) {
    fun observeForCustomer(customerId: Int) = dao.observeForCustomer(customerId)
    fun observeRecentForCustomer(customerId: Int) = dao.observeRecentForCustomer(customerId)
    fun observeNetDue(customerId: Int) = dao.observeNetDue(customerId)
    fun observeTotals(customerId: Int) = dao.observeTotals(customerId)
    fun observeForDate(start: Long, end: Long) = dao.observeForDate(start, end)
    fun observeTotalsForDate(start: Long, end: Long) = dao.observeTotalsForDate(start, end)
    fun observePaymentTotalForDate(start: Long, end: Long) = dao.observePaymentTotalForDate(start, end)
    suspend fun add(transaction: LedgerTransaction) = dao.insert(transaction)
    suspend fun delete(transaction: LedgerTransaction) = dao.delete(transaction)
    suspend fun clear() = dao.deleteAll()
}
