package com.gabrielgrs1.pokedex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.gabrielgrs1.pokedex.presentation.uistate.HomeUiState
import com.gabrielgrs1.pokedex.core.theme.PokedexTheme
import com.gabrielgrs1.pokedex.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KoinContext {
                        GreetingRoute(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(homeUiState: HomeUiState, modifier: Modifier = Modifier) {
    if (homeUiState.content.isNotEmpty())
    Text(
        text = "Hello world! ${homeUiState.content[0].name}",
        modifier = modifier
    )
}

@Composable
fun GreetingRoute(homeViewModel: HomeViewModel = koinViewModel(), modifier: Modifier = Modifier) {
    val uiState by homeViewModel.uiState.collectAsState()
    Greeting(homeUiState = uiState, modifier)
}