package br.com.cultiva.domain.model

enum class OrderStatus(val label: String) {
    CRITICO("CRÍTICO"),
    URGENTE("URGENTE"),
    ALERTA("ALERTA")
}

data class Order(
    val id: Int,
    val highway: String,
    val status: OrderStatus,
    val extension: String,
    val type: String,
    val attended: Boolean,
    val completed: Boolean = false,
    val hasEvidence: Boolean = false
)
