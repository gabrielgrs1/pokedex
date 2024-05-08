package com.gabrielgrs1.pokedex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gabrielgrs1.pokedex.core.theme.PokedexTheme
import com.gabrielgrs1.pokedex.presentation.components.AppNavHost
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            PokedexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    KoinContext {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

