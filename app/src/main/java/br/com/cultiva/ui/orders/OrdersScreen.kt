package br.com.cultiva.ui.orders

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cultiva.domain.model.Order
import br.com.cultiva.domain.model.UiState
import br.com.cultiva.ui.components.AppHeader
import br.com.cultiva.ui.components.BottomBar
import br.com.cultiva.ui.theme.Border
import br.com.cultiva.ui.theme.Green
import br.com.cultiva.ui.theme.color

@Composable
fun OrdersScreen(
    state: UiState<List<Order>>,
    onOrder: (Order) -> Unit,
    onHistory: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppHeader("Lista de Ordens")
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            when (state) {
                is UiState.Loading -> CircularProgressIndicator(color = Green)
                is UiState.Error -> Text(state.message, color = Color(0xFFD32F2F), fontSize = 10.sp)
                is UiState.Success -> {
                    val pending = state.data.filter { !it.completed }
                    if (pending.isEmpty()) {
                        Text("Nenhuma ordem pendente.", fontSize = 10.sp, color = Color.DarkGray)
                    } else {
                        Column(
                            Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pending.forEach { OrderCard(it, onClick = { onOrder(it) }) }
                        }
                    }
                }
            }
        }
        BottomBar(ordersActive = true, onOrders = {}, onHistory = onHistory)
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().height(80.dp).border(1.dp, Border).padding(4.dp).clickable { onClick() }
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("RODOVIA", fontSize = 7.sp)
                Text(order.highway, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                "◉ ${order.status.label}", color = Color.White, fontSize = 7.sp,
                modifier = Modifier.background(order.status.color()).padding(horizontal = 5.dp, vertical = 2.dp)
            )
        }
        Row(Modifier.fillMaxWidth().padding(top = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("EXTENSÃO", fontSize = 5.sp); Text(order.extension, fontSize = 7.sp, fontWeight = FontWeight.Bold) }
            Column(horizontalAlignment = Alignment.End) { Text("TIPO", fontSize = 5.sp); Text(order.type, fontSize = 7.sp, fontWeight = FontWeight.Bold) }
        }
        if (!order.attended) {
            Button(
                onClick = onClick, modifier = Modifier.fillMaxWidth().height(17.dp),
                shape = RoundedCornerShape(2.dp), contentPadding = ButtonDefaults.ContentPadding,
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) { Text("Iniciar Trabalho", fontSize = 8.sp) }
        } else {
            Text("Em andamento", fontSize = 7.sp, modifier = Modifier.align(Alignment.End))
        }
    }
}
