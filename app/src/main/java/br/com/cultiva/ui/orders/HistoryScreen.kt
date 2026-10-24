package br.com.cultiva.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun HistoryScreen(
    state: UiState<List<Order>>,
    onOrders: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppHeader("Histórico")
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            val done = (state as? UiState.Success)?.data?.filter { it.completed }.orEmpty()
            if (done.isEmpty()) {
                Text("Nenhum atendimento finalizado.", fontSize = 10.sp, color = Color.DarkGray)
            } else {
                Column(
                    Modifier.fillMaxSize().padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    done.forEach { HistoryCard(it) }
                }
            }
        }
        BottomBar(ordersActive = false, onOrders = onOrders, onHistory = {})
    }
}

@Composable
private fun HistoryCard(order: Order) {
    Column(Modifier.fillMaxWidth().height(62.dp).border(1.dp, Border).padding(4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("RODOVIA", fontSize = 7.sp); Text(order.highway, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            Text("FEITO", color = Color.White, fontSize = 7.sp, modifier = Modifier.background(Green).padding(horizontal = 10.dp, vertical = 2.dp))
        }
        Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("EXTENSÃO", fontSize = 5.sp); Text(order.extension, fontSize = 7.sp, fontWeight = FontWeight.Bold) }
            Column(horizontalAlignment = Alignment.End) { Text("TIPO", fontSize = 5.sp); Text(order.type, fontSize = 7.sp, fontWeight = FontWeight.Bold) }
        }
    }
}
