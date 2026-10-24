package br.com.cultiva.data.repository

import br.com.cultiva.data.local.OrderDao
import br.com.cultiva.data.local.OrderEntity
import br.com.cultiva.data.mapper.toDomain
import br.com.cultiva.data.mapper.toEntity
import br.com.cultiva.domain.model.Order
import br.com.cultiva.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(private val dao: OrderDao) : OrderRepository {

    override fun observeOrders(): Flow<List<Order>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun updateOrder(order: Order) {
        dao.update(order.toEntity())
    }

    override suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(defaultOrders())
        }
    }

    private fun defaultOrders(): List<OrderEntity> = listOf(
        OrderEntity(1, "SP-021", "CRITICO", "km 4 - 4.5", "Apenas manual", false, false, false),
        OrderEntity(2, "SP-021", "URGENTE", "km 5 - 5.5", "Mecanizado", true, false, true),
        OrderEntity(3, "SP-021", "ALERTA", "km 6 - 6.5", "Apenas manual", false, false, false)
    )
}
