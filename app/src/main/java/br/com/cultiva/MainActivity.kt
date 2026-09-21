package br.com.cultiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF2B6733)
private val Dark = Color(0xFF1E1E1E)
private val Paper = Color(0xFFF5F5F5)
private val Border = Color(0xFF555555)

private enum class Screen { Login, Orders, Detail, History }

private data class Order(
    val status: String,
    val color: Color,
    val extension: String,
    val type: String,
    val attended: Boolean
)

private val orders = listOf(
    Order("CRÍTICO", Color(0xFFE95A61), "km 4 - 4.5", "Apenas manual", false),
    Order("URGENTE", Color(0xFFF1B633), "km 5 - 5.5", "Mecanizado", true),
    Order("ALERTA", Color(0xFFE1D34F), "km 6 - 6.5", "Apenas manual", false)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CultivaApp() }
    }
}

@Composable
private fun CultivaApp() {
    var screen by remember { mutableStateOf(Screen.Login) }
    var selectedOrder by remember { mutableStateOf(orders[0]) }

    MaterialTheme {
        Surface(Modifier.fillMaxSize().background(Dark), color = Dark) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.width(300.dp).fillMaxHeight(),
                    color = Paper
                ) {
                    when (screen) {
                        Screen.Login -> LoginScreen { screen = Screen.Orders }
                        Screen.Orders -> OrdersScreen(
                            onOrder = { order -> selectedOrder = order; screen = Screen.Detail },
                            onHistory = { screen = Screen.History }
                        )
                        Screen.Detail -> DetailScreen(
                            order = selectedOrder,
                            onBack = { screen = Screen.Orders },
                            onHistory = { screen = Screen.History }
                        )
                        Screen.History -> HistoryScreen(
                            onOrders = { screen = Screen.Orders }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(onLogin: () -> Unit) {
    var email by remember { mutableStateOf("email@teste.com") }
    var password by remember { mutableStateOf("123456789012") }
    Column(Modifier.fillMaxSize().padding(horizontal = 17.dp), verticalArrangement = Arrangement.Center) {
        Text("Login", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Green),
                contentAlignment = Alignment.Center
            ) { Text("✂", color = Color.White, fontSize = 29.sp) }
            Text("Cultiva", color = Green, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        }
        Text("E-mail", fontSize = 7.sp, color = Color.DarkGray)
        OutlinedTextField(
            value = email, onValueChange = { email = it }, singleLine = true,
            modifier = Modifier.fillMaxWidth().height(34.dp), textStyle = MaterialTheme.typography.bodySmall,
            shape = RoundedCornerShape(4.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text("Senha", fontSize = 7.sp, color = Color.DarkGray)
        OutlinedTextField(
            value = password, onValueChange = { password = it }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().height(34.dp), textStyle = MaterialTheme.typography.bodySmall,
            shape = RoundedCornerShape(4.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onLogin, modifier = Modifier.fillMaxWidth().height(32.dp),
            shape = RoundedCornerShape(4.dp), contentPadding = ButtonDefaults.ContentPadding,
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) { Text("Entrar", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun AppHeader(title: String, back: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().height(26.dp).background(Color.White).border(1.dp, Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (back != null) Text("‹", color = Green, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp, end = 15.dp).clickable { back() })
        Text(title, color = Green, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        if (back != null) Spacer(Modifier.width(24.dp))
    }
}

@Composable
private fun BottomBar(selected: Screen, onOrders: () -> Unit, onHistory: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(34.dp).background(Color.White).border(1.dp, Color.LightGray), horizontalArrangement = Arrangement.SpaceEvenly) {
        BottomItem("▤", "Ordens", selected == Screen.Orders, onOrders)
        BottomItem("▣", "Histórico", selected == Screen.History, onHistory)
    }
}

@Composable
private fun BottomItem(icon: String, label: String, active: Boolean, onClick: () -> Unit) {
    Column(Modifier.width(54.dp).fillMaxHeight().clickable { onClick() }.background(if (active) Green else Color.White), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(icon, color = if (active) Color.White else Color.DarkGray, fontSize = 14.sp)
        Text(label, color = if (active) Color.White else Color.DarkGray, fontSize = 6.sp)
    }
}

@Composable
private fun OrdersScreen(onOrder: (Order) -> Unit, onHistory: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Lista de ordens", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.background(Dark).fillMaxWidth().padding(8.dp, 11.dp))
        AppHeader("Lista de Ordens")
        Column(Modifier.weight(1f).padding(horizontal = 10.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            orders.forEach { order -> OrderCard(order, onClick = { onOrder(order) }) }
        }
        BottomBar(Screen.Orders, onOrders = {}, onHistory = onHistory)
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().height(80.dp).border(1.dp, Border).padding(4.dp).clickable { onClick() }) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("RODOVIA", fontSize = 7.sp); Text("SP-021", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            Text("◉ ${order.status}", color = Color.White, fontSize = 7.sp, modifier = Modifier.background(order.color).padding(horizontal = 5.dp, vertical = 2.dp))
        }
        Row(Modifier.fillMaxWidth().padding(top = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("EXTENSÃO", fontSize = 5.sp); Text(order.extension, fontSize = 7.sp, fontWeight = FontWeight.Bold) }
            Column(horizontalAlignment = Alignment.End) { Text("TIPO", fontSize = 5.sp); Text(order.type, fontSize = 7.sp, fontWeight = FontWeight.Bold) }
        }
        if (!order.attended) Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(17.dp), shape = RoundedCornerShape(2.dp), contentPadding = ButtonDefaults.ContentPadding, colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("Iniciar Trabalho", fontSize = 8.sp) }
        else Text("Em andamento", fontSize = 7.sp, modifier = Modifier.align(Alignment.End))
    }
}

@Composable
private fun DetailScreen(order: Order, onBack: () -> Unit, onHistory: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        AppHeader("Detalhe da ordem", onBack)
        Column(Modifier.weight(1f).padding(7.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 7.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("EXTENSÃO", fontSize = 5.sp); Text(order.extension, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
                Column(horizontalAlignment = Alignment.End) { Text("${order.status}", color = Color.White, fontSize = 7.sp, modifier = Modifier.background(order.color).padding(4.dp, 2.dp)); Text(if (order.attended) "Em andamento" else "", fontSize = 7.sp) }
            }
            InfoBox("TIPO DE ROÇADA", "Apenas manual", Modifier.fillMaxWidth().height(39.dp))
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(26.dp)) {
                InfoBox("EXTENSÃO", "500m", Modifier.weight(1f).height(48.dp))
                InfoBox("LADO", "NORTE", Modifier.weight(1f).height(48.dp))
            }
            if (order.attended) RoadPreview() else UploadBox()
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(32.dp), shape = RoundedCornerShape(2.dp), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("Abrir no Maps", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(5.dp))
            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(32.dp), shape = RoundedCornerShape(2.dp), contentPadding = ButtonDefaults.ContentPadding) { Text(if (order.attended) "Finalizar atendimento" else "Iniciar atendimento", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
        }
        BottomBar(Screen.Orders, onOrders = onBack, onHistory = onHistory)
    }
}

@Composable
private fun InfoBox(label: String, value: String, modifier: Modifier) {
    Column(modifier.border(1.dp, Border).padding(4.dp), verticalArrangement = Arrangement.Center) { Text(label, fontSize = 6.sp); Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun UploadBox() {
    Box(Modifier.fillMaxWidth().height(69.dp).padding(horizontal = 26.dp, vertical = 12.dp).border(1.dp, Green), contentAlignment = Alignment.Center) { Text("FAZER UPLOAD", fontSize = 7.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun RoadPreview() {
    Canvas(Modifier.fillMaxWidth().height(69.dp).padding(horizontal = 26.dp, vertical = 12.dp)) {
        drawRect(Color(0xFF75B3D4))
        drawRect(Color(0xFFB5C879), topLeft = Offset(0f, size.height * .55f), size = androidx.compose.ui.geometry.Size(size.width, size.height * .45f))
        val road = Path().apply { moveTo(size.width * .35f, size.height); lineTo(size.width * .52f, 0f); lineTo(size.width * .68f, 0f); lineTo(size.width * .9f, size.height); close() }
        drawPath(road, Color.DarkGray)
        drawLine(Color.White, Offset(size.width * .58f, 0f), Offset(size.width * .62f, size.height), 2f, StrokeCap.Round)
        drawLine(Color.White, Offset(size.width * .48f, size.height), Offset(size.width * .54f, 0f), 2f, StrokeCap.Round)
    }
}

@Composable
private fun HistoryScreen(onOrders: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Histórico", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.background(Dark).fillMaxWidth().padding(8.dp, 11.dp))
        AppHeader("Histórico")
        Column(Modifier.weight(1f).padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            orders.map { it.copy(attended = true) }.forEach { order ->
                Column(Modifier.fillMaxWidth().height(62.dp).border(1.dp, Border).padding(4.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Column { Text("RODOVIA", fontSize = 7.sp); Text("SP-021", fontSize = 12.sp, fontWeight = FontWeight.Bold) }; Text("FEITO", color = Color.White, fontSize = 7.sp, modifier = Modifier.background(Green).padding(horizontal = 10.dp, vertical = 2.dp)) }
                    Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { Column { Text("EXTENSÃO", fontSize = 5.sp); Text(order.extension, fontSize = 7.sp, fontWeight = FontWeight.Bold) }; Column(horizontalAlignment = Alignment.End) { Text("TIPO", fontSize = 5.sp); Text(order.type, fontSize = 7.sp, fontWeight = FontWeight.Bold) } }
                }
            }
        }
        BottomBar(Screen.History, onOrders = onOrders, onHistory = {})
    }
}
