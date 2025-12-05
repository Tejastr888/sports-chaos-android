package com.shk.sportschaos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shk.sportschaos.data.local.TokenManager
import com.shk.sportschaos.ui.navigation.NavGraph
import com.shk.sportschaos.ui.theme.SportschaosTheme

// ══════════════════════════════════════════════════════════════
// MAIN ACTIVITY - Entry point of your app
// This is where Android starts your application
// ══════════════════════════════════════════════════════════════
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Initialize TokenManager
        val tokenManager = TokenManager(applicationContext)

        // ════════════════════════════════════════════════════
        // SET CONTENT - Define UI
        // Everything inside setContent is Jetpack Compose
        // ════════════════════════════════════════════════════
        setContent {
            // ═══ THEME - Apply Material Design colors/styles ═══
            SportschaosTheme {
                // ═══ SURFACE - Background container ═══
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ═══ NAVIGATION CONTROLLER ═══
                    val navController = rememberNavController()

                    // ═══ NAVIGATION GRAPH ═══
                    NavGraph(
                        navController = navController,
                        tokenManager = tokenManager
                    )
                }
            }
        }
    }
}