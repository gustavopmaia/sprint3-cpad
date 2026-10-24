package br.com.cultiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import br.com.cultiva.navigation.CultivaNavGraph
import br.com.cultiva.ui.theme.Paper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val container = (application as CultivaApp).container
        setContent {
            MaterialTheme {
                Surface(
                    Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars),
                    color = Paper
                ) {
                    CultivaNavGraph(container)
                }
            }
        }
    }
}
