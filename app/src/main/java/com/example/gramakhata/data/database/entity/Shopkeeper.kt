package com.example.gramakhata.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopkeeper")
data class Shopkeeper(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shopName: String,
    val ownerName: String,
    val mobileNumber: String,
    val pinHash: String,
    val createdAt: Long = System.currentTimeMillis()
)
