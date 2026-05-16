package com.example.gramakhata.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gramakhata.data.database.dao.CustomerDao
import com.example.gramakhata.data.database.dao.ShopkeeperDao
import com.example.gramakhata.data.database.dao.TransactionDao
import com.example.gramakhata.data.database.entity.Customer
import com.example.gramakhata.data.database.entity.LedgerTransaction
import com.example.gramakhata.data.database.entity.Shopkeeper

@Database(
    entities = [Shopkeeper::class, Customer::class, LedgerTransaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopkeeperDao(): ShopkeeperDao
    abstract fun customerDao(): CustomerDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grama_khata_database"
                ).build().also { INSTANCE = it }
            }
    }
}
