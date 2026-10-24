package br.com.cultiva.domain.repository

import br.com.cultiva.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeOrders(): Flow<List<Order>>
    suspend fun updateOrder(order: Order)
    suspend fun seedIfEmpty()
}
