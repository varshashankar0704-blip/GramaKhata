package com.example.gramakhata.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class TransactionType { CREDIT, PAYMENT, REMINDER }

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["customerId"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId")]
)
data class LedgerTransaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val customerId: Int,
    val type: TransactionType,
    val amount: Double,
    val note: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
