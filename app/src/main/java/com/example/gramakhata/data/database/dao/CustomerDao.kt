package com.example.gramakhata.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gramakhata.data.database.entity.Customer

data class CustomerBalance(
    val customerId: Int,
    val name: String,
    val phoneNumber: String,
    val photoPath: String?,
    val createdAt: Long,
    val netDue: Double
)

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: Customer): Long

    @Update
    suspend fun update(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("SELECT * FROM customers WHERE customerId = :id")
    fun observeCustomer(id: Int): LiveData<Customer?>

    @Query("SELECT * FROM customers WHERE customerId = :id")
    suspend fun getCustomer(id: Int): Customer?

    @Query("SELECT COUNT(*) FROM customers WHERE phoneNumber = :phone AND (:exceptId IS NULL OR customerId != :exceptId)")
    suspend fun countByPhone(phone: String, exceptId: Int? = null): Int

    @Query(
        """
        SELECT c.customerId, c.name, c.phoneNumber, c.photoPath, c.createdAt,
        COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount WHEN t.type = 'PAYMENT' THEN -t.amount ELSE 0 END), 0) AS netDue
        FROM customers c
        LEFT JOIN transactions t ON t.customerId = c.customerId
        GROUP BY c.customerId
        ORDER BY netDue DESC, c.name COLLATE NOCASE ASC
        """
    )
    fun observeCustomerBalances(): LiveData<List<CustomerBalance>>

    @Query("DELETE FROM customers")
    suspend fun deleteAll()
}
