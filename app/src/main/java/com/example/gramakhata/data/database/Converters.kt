package com.example.gramakhata.data.database

import androidx.room.TypeConverter
import com.example.gramakhata.data.database.entity.TransactionType

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}
