package com.example.gramakhata.data.repository

import com.example.gramakhata.data.database.dao.CustomerDao
import com.example.gramakhata.data.database.entity.Customer

class CustomerRepository(private val dao: CustomerDao) {
    fun observeCustomer(id: Int) = dao.observeCustomer(id)
    fun observeCustomerBalances() = dao.observeCustomerBalances()
    suspend fun getCustomer(id: Int) = dao.getCustomer(id)
    suspend fun add(customer: Customer) = dao.insert(customer)
    suspend fun update(customer: Customer) = dao.update(customer)
    suspend fun delete(customer: Customer) = dao.delete(customer)
    suspend fun phoneExists(phone: String, exceptId: Int? = null) = dao.countByPhone(phone, exceptId) > 0
    suspend fun clear() = dao.deleteAll()
}
