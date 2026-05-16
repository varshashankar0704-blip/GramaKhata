package com.example.gramakhata.data.repository

import com.example.gramakhata.data.database.dao.ShopkeeperDao
import com.example.gramakhata.data.database.entity.Shopkeeper

class ShopkeeperRepository(private val dao: ShopkeeperDao) {
    fun observeShopkeeper() = dao.observeShopkeeper()
    suspend fun getShopkeeper() = dao.getShopkeeper()
    suspend fun register(shopkeeper: Shopkeeper) = dao.insert(shopkeeper)
    suspend fun update(shopkeeper: Shopkeeper) = dao.update(shopkeeper)
    suspend fun clear() = dao.deleteAll()
}
