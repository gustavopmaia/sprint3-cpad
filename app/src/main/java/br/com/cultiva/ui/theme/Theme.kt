package br.com.cultiva.ui.theme

import androidx.compose.ui.graphics.Color
import br.com.cultiva.domain.model.OrderStatus

val Green = Color(0xFF2B6733)
val Paper = Color(0xFFF5F5F5)
val Border = Color(0xFF555555)

fun OrderStatus.color(): Color = when (this) {
    OrderStatus.CRITICO -> Color(0xFFE95A61)
    OrderStatus.URGENTE -> Color(0xFFF1B633)
    OrderStatus.ALERTA -> Color(0xFFE1D34F)
}
