package com.example.gramakhata.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gramakhata.data.database.entity.Shopkeeper

@Dao
interface ShopkeeperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shopkeeper: Shopkeeper): Long

    @Update
    suspend fun update(shopkeeper: Shopkeeper)

    @Query("SELECT * FROM shopkeeper LIMIT 1")
    suspend fun getShopkeeper(): Shopkeeper?

    @Query("SELECT * FROM shopkeeper LIMIT 1")
    fun observeShopkeeper(): LiveData<Shopkeeper?>

    @Query("DELETE FROM shopkeeper")
    suspend fun deleteAll()
}
