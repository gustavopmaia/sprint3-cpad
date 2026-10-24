package br.com.cultiva.domain.usecase

import br.com.cultiva.domain.model.Order
import br.com.cultiva.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrdersUseCase(private val repository: OrderRepository) {
    operator fun invoke(): Flow<List<Order>> = repository.observeOrders()
}

class UpdateOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) = repository.updateOrder(order)
}

class StartOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) {
        require(!order.completed) { "Ordem já finalizada não pode ser iniciada." }
        repository.updateOrder(order.copy(attended = true))
    }
}

class FinishOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) {
        require(order.attended) { "Inicie o atendimento antes de finalizar." }
        repository.updateOrder(order.copy(attended = false, completed = true))
    }
}

class AddEvidenceUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order) {
        repository.updateOrder(order.copy(hasEvidence = true, attended = true))
    }
}

class SeedOrdersUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke() = repository.seedIfEmpty()
}
