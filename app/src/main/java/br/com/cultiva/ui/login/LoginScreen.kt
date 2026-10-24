package br.com.cultiva.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.cultiva.ui.theme.Border
import br.com.cultiva.ui.theme.Green

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoggedIn: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf(viewModel.savedEmail.ifBlank { "email@teste.com" }) }
    var password by rememberSaveable { mutableStateOf("123456789012") }

    LaunchedEffect(state.success) {
        if (state.success) onLoggedIn()
    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 17.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Green),
                contentAlignment = Alignment.Center
            ) { Text("✂", color = Color.White, fontSize = 29.sp) }
            Text(
                "Cultiva", color = Green, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
        Text("E-mail", fontSize = 7.sp, color = Color.DarkGray)
        OutlinedTextField(
            value = email, onValueChange = { email = it }, singleLine = true,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
            shape = RoundedCornerShape(4.dp),
            colors = fieldColors()
        )
        Spacer(Modifier.height(8.dp))
        Text("Senha", fontSize = 7.sp, color = Color.DarkGray)
        OutlinedTextField(
            value = password, onValueChange = { password = it }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().height(48.dp),
            textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
            shape = RoundedCornerShape(4.dp),
            colors = fieldColors()
        )
        if (state.error != null) {
            Spacer(Modifier.height(6.dp))
            Text(state.error!!, color = Color(0xFFD32F2F), fontSize = 8.sp)
        }
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = { viewModel.login(email, password) },
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth().height(32.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            if (state.loading) {
                CircularProgressIndicator(Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Entrar", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun fieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = Green,
    focusedIndicatorColor = Color(0xFF168BF0),
    unfocusedIndicatorColor = Border
)
