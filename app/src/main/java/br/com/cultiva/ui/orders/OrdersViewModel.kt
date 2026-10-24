package br.com.cultiva.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.cultiva.domain.model.Order
import br.com.cultiva.domain.model.UiState
import br.com.cultiva.domain.usecase.AddEvidenceUseCase
import br.com.cultiva.domain.usecase.FinishOrderUseCase
import br.com.cultiva.domain.usecase.GetOrdersUseCase
import br.com.cultiva.domain.usecase.SeedOrdersUseCase
import br.com.cultiva.domain.usecase.StartOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdersViewModel(
    getOrders: GetOrdersUseCase,
    private val startOrder: StartOrderUseCase,
    private val finishOrder: FinishOrderUseCase,
    private val addEvidence: AddEvidenceUseCase,
    private val seedOrders: SeedOrdersUseCase
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    val ordersState: StateFlow<UiState<List<Order>>> =
        getOrders()
            .map<List<Order>, UiState<List<Order>>> { UiState.Success(it) }
            .catch { emit(UiState.Error(it.message ?: "Erro ao carregar ordens.")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    init {
        viewModelScope.launch {
            runCatching { seedOrders() }
                .onFailure { _message.value = "Erro ao preparar dados." }
        }
    }

    fun start(order: Order) = run { startOrder(order) }
    fun finish(order: Order) = run { finishOrder(order) }
    fun evidence(order: Order) = run { addEvidence(order) }

    private fun run(action: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching { action() }
                .onFailure { _message.value = it.message ?: "Operação inválida." }
        }
    }

    fun consumeMessage() {
        _message.value = null
    }

    class Factory(
        private val getOrders: GetOrdersUseCase,
        private val startOrder: StartOrderUseCase,
        private val finishOrder: FinishOrderUseCase,
        private val addEvidence: AddEvidenceUseCase,
        private val seedOrders: SeedOrdersUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            OrdersViewModel(getOrders, startOrder, finishOrder, addEvidence, seedOrders) as T
    }
}
