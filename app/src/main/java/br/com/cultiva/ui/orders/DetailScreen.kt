package br.com.cultiva.ui.orders

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cultiva.domain.model.Order
import br.com.cultiva.ui.components.AppHeader
import br.com.cultiva.ui.components.BottomBar
import br.com.cultiva.ui.components.InfoBox
import br.com.cultiva.ui.theme.Green
import br.com.cultiva.ui.theme.color

@Composable
fun DetailScreen(
    order: Order,
    onBack: () -> Unit,
    onHistory: () -> Unit,
    onEvidence: () -> Unit,
    onStart: () -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        AppHeader("Detalhe da ordem", onBack)
        Column(Modifier.weight(1f).padding(7.dp)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column { Text("EXTENSÃO", fontSize = 5.sp); Text(order.extension, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
                Column(horizontalAlignment = Alignment.End) {
                    Text(order.status.label, color = Color.White, fontSize = 7.sp, modifier = Modifier.background(order.status.color()).padding(4.dp, 2.dp))
                    Text(if (order.attended) "Em andamento" else "", fontSize = 7.sp)
                }
            }
            InfoBox("TIPO DE ROÇADA", order.type, Modifier.fillMaxWidth().height(72.dp))
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoBox("EXTENSÃO", "500m", Modifier.weight(1f).height(72.dp))
                InfoBox("LADO", "NORTE", Modifier.weight(1f).height(72.dp))
            }
            if (order.hasEvidence) RoadPreview() else UploadBox(onClick = onEvidence)
            Button(
                onClick = {
                    val intent = android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("geo:0,0?q=Rodovia+${order.highway}")
                    )
                    if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(32.dp),
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) { Text("Abrir no Maps", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(5.dp))
            OutlinedButton(
                onClick = if (order.attended) onFinish else onStart,
                modifier = Modifier.fillMaxWidth().height(32.dp),
                shape = RoundedCornerShape(2.dp),
                contentPadding = ButtonDefaults.ContentPadding
            ) {
                Text(
                    if (order.attended) "Finalizar atendimento" else "Iniciar atendimento",
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black
                )
            }
        }
        BottomBar(ordersActive = true, onOrders = onBack, onHistory = onHistory)
    }
}

@Composable
private fun UploadBox(onClick: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().height(69.dp).padding(horizontal = 26.dp, vertical = 12.dp)
            .border(1.dp, Green).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text("FAZER UPLOAD", fontSize = 7.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun RoadPreview() {
    Canvas(Modifier.fillMaxWidth().height(69.dp).padding(horizontal = 26.dp, vertical = 12.dp)) {
        drawRect(Color(0xFF75B3D4))
        drawRect(Color(0xFFB5C879), topLeft = Offset(0f, size.height * .55f), size = Size(size.width, size.height * .45f))
        val road = Path().apply {
            moveTo(size.width * .35f, size.height)
            lineTo(size.width * .52f, 0f)
            lineTo(size.width * .68f, 0f)
            lineTo(size.width * .9f, size.height)
            close()
        }
        drawPath(road, Color.DarkGray)
        drawLine(Color.White, Offset(size.width * .58f, 0f), Offset(size.width * .62f, size.height), 2f, StrokeCap.Round)
        drawLine(Color.White, Offset(size.width * .48f, size.height), Offset(size.width * .54f, 0f), 2f, StrokeCap.Round)
    }
}
