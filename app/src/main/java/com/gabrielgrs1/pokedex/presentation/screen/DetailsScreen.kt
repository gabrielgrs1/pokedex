package com.gabrielgrs1.pokedex.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.gabrielgrs1.pokedex.R
import com.gabrielgrs1.pokedex.core.utils.formatToUserFriendly
import com.gabrielgrs1.pokedex.presentation.components.Error
import com.gabrielgrs1.pokedex.presentation.components.Loading
import com.gabrielgrs1.pokedex.presentation.uistate.DetailsUiState
import com.gabrielgrs1.pokedex.presentation.viewmodel.DetailsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    uiState: DetailsUiState,
    onBackPressed: () -> Unit = {},
    loadingProgress: Float,
) {
    when {
        uiState.pokemon != null -> {
            ContentState(uiState, modifier, onBackPressed)
        }

        uiState.isLoading -> Loading(currentProgressLoading = loadingProgress)
        uiState.isError -> Error()
    }
}


@Composable
private fun ContentState(
    uiState: DetailsUiState,
    modifier: Modifier,
    onBackPressed: () -> Unit = {},
) {
    val pokemon = uiState.pokemon

    pokemon?.let { pokemonDetail ->
        Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 48.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back Button
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    // Pokemon Name
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = pokemonDetail.name.formatToUserFriendly(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFE3350D)
                    )

                    // Pokemon Id
                    Text(
                        text = "#${pokemonDetail.id}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Blue
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.imageUrl)
                    .dispatcher(Dispatchers.IO)
                    .memoryCacheKey(pokemon.imageUrl)
                    .diskCacheKey(pokemon.imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build()

                // Pokemon Image
                AsyncImage(
                    modifier = modifier.size(480.dp),
                    model = imageRequest,
                    contentDescription = "image at ${pokemon.name}",
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Weight
                Text(
                    text = stringResource(id = R.string.weight_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )
                Text(
                    text = "${pokemonDetail.weight} kg",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Height
                Text(
                    text = stringResource(id = R.string.height_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )
                Text(
                    text = "${pokemonDetail.height} M",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Types
                Text(
                    text = stringResource(id = R.string.types_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )
                pokemonDetail.types.forEach { type ->
                    Text(
                        text = type.formatToUserFriendly(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Stats
                Text(
                    text = stringResource(id = R.string.stats_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )
                pokemonDetail.stats.forEach { stat ->
                    Text(
                        text = stat.formatToUserFriendly(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Abilities
                Text(
                    text = stringResource(id = R.string.abilities_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray
                )
                pokemonDetail.abilities.forEach { ability ->
                    Text(
                        text = ability.formatToUserFriendly(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsScreenRoute(
    modifier: Modifier = Modifier,
    pokemonName: String,
    detailsViewModel: DetailsViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {}
) {
    val uiState by detailsViewModel.uiState.collectAsState()

    val currentProgress by remember {
        mutableFloatStateOf(2f)
    } // TODO: Maybe this should be in the viewmodel

    LaunchedEffect(Unit) {
        detailsViewModel.getPokemon(pokemonName)
    }

    DetailsScreen(
        uiState = uiState,
        modifier = modifier,
        onBackPressed = onBackPressed,
        loadingProgress = currentProgress
    )
}