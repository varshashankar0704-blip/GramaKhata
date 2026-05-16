package com.example.gramakhata.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true) val customerId: Int = 0,
    val name: String,
    val phoneNumber: String,
    val photoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
