package br.com.cultiva.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cultiva.ui.theme.Border
import br.com.cultiva.ui.theme.Green

@Composable
fun AppHeader(title: String, back: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().height(48.dp).background(Color.White).border(1.dp, Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (back != null) {
            Box(
                modifier = Modifier.size(48.dp).clickable { back() },
                contentAlignment = Alignment.Center
            ) { Text("‹", color = Green, fontSize = 32.sp) }
        }
        Text(
            title, color = Green, fontSize = 12.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f), textAlign = TextAlign.Center
        )
        if (back != null) Spacer(Modifier.width(48.dp))
    }
}

@Composable
fun BottomBar(ordersActive: Boolean, onOrders: () -> Unit, onHistory: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(34.dp).background(Color.White).border(1.dp, Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomItem("▤", "Ordens", ordersActive, onOrders)
        BottomItem("▣", "Histórico", !ordersActive, onHistory)
    }
}

@Composable
private fun BottomItem(icon: String, label: String, active: Boolean, onClick: () -> Unit) {
    Column(
        Modifier.width(54.dp).fillMaxHeight().clickable { onClick() }
            .background(if (active) Green else Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(icon, color = if (active) Color.White else Color.DarkGray, fontSize = 14.sp)
        Text(label, color = if (active) Color.White else Color.DarkGray, fontSize = 6.sp)
    }
}

@Composable
fun InfoBox(label: String, value: String, modifier: Modifier) {
    Column(modifier.border(1.dp, Border).padding(8.dp), verticalArrangement = Arrangement.Center) {
        Text(label, fontSize = 7.sp, color = Color.DarkGray, maxLines = 1)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
    }
}
