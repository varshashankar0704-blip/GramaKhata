package com.example.gramakhata.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gramakhata.data.database.entity.LedgerTransaction

data class Totals(val credit: Double, val payment: Double)

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: LedgerTransaction): Long

    @Delete
    suspend fun delete(transaction: LedgerTransaction)

    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun observeForCustomer(customerId: Int): LiveData<List<LedgerTransaction>>

    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY createdAt DESC LIMIT 5")
    fun observeRecentForCustomer(customerId: Int): LiveData<List<LedgerTransaction>>

    @Query("SELECT COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount WHEN type = 'PAYMENT' THEN -amount ELSE 0 END), 0) FROM transactions WHERE customerId = :customerId")
    fun observeNetDue(customerId: Int): LiveData<Double>

    @Query("SELECT COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE 0 END), 0) AS credit, COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) AS payment FROM transactions WHERE customerId = :customerId")
    fun observeTotals(customerId: Int): LiveData<Totals>

    @Query("SELECT * FROM transactions WHERE createdAt BETWEEN :start AND :end ORDER BY createdAt DESC")
    fun observeForDate(start: Long, end: Long): LiveData<List<LedgerTransaction>>

    @Query("SELECT COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE 0 END), 0) AS credit, COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) AS payment FROM transactions WHERE createdAt BETWEEN :start AND :end")
    fun observeTotalsForDate(start: Long, end: Long): LiveData<Totals>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'PAYMENT' AND createdAt BETWEEN :start AND :end")
    fun observePaymentTotalForDate(start: Long, end: Long): LiveData<Double>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
