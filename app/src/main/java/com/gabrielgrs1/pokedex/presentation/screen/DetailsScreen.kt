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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.gabrielgrs1.pokedex.R
import com.gabrielgrs1.pokedex.core.utils.formatToUserFriendly
import com.gabrielgrs1.pokedex.presentation.components.ErrorView
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
    onFavoritePressed: (Boolean) -> Unit = {},
) {
    val errorMessage = if (uiState.errorMessage.isEmpty().not()) uiState.errorMessage else null
    when {
        uiState.pokemon != null -> {
            ContentState(
                uiState = uiState,
                modifier = modifier,
                onBackPressed = onBackPressed,
                onFavoritePressed = onFavoritePressed
            )
        }

        uiState.isLoading -> Loading()
        uiState.isError -> ErrorView(errorMessage)
    }
}


@Composable
private fun ContentState(
    uiState: DetailsUiState,
    modifier: Modifier,
    onBackPressed: () -> Unit = {},
    onFavoritePressed: (Boolean) -> Unit = {},
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF005800),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(36.dp)
                        )
                    }

                    // Pokemon Name
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = pokemonDetail.name.formatToUserFriendly(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF005800)
                    )

                    // Pokemon Id
                    Text(
                        text = "#${pokemonDetail.id}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF005800),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Favorite Button
                IconButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    onClick = { onFavoritePressed(uiState.pokemon.isFavorite.not()) }) {
                    val favoriteIcon = if (uiState.pokemon.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    }

                    val tint = if (uiState.pokemon.isFavorite) {
                        Color.Red
                    } else {
                        Color(0xFF005800)
                    }

                    Icon(
                        modifier = Modifier
                            .size(36.dp),
                        imageVector = favoriteIcon,
                        contentDescription = "Favorite",
                        tint = tint
                    )
                }

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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Weight
                        Text(
                            text = stringResource(id = R.string.weight_label),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF005800),
                        )
                        Text(
                            text = "${pokemonDetail.weight} kg",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Height
                        Text(
                            text = stringResource(id = R.string.height_label),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF005800),
                        )
                        Text(
                            text = "${pokemonDetail.height} m",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Types
                        Text(
                            text = stringResource(id = R.string.types_label),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF005800),
                            textAlign = TextAlign.Center,
                        )
                        pokemonDetail.types.forEach { type ->
                            Text(
                                text = type.formatToUserFriendly(),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Stats
                        Text(
                            text = stringResource(id = R.string.stats_label),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF005800),
                        )
                        pokemonDetail.stats.forEach { stat ->
                            Text(
                                text = stat.formatToUserFriendly(),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Abilities
                        Text(
                            text = stringResource(id = R.string.abilities_label),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF005800),
                        )
                        pokemonDetail.abilities.forEach { ability ->
                            Text(
                                text = ability.formatToUserFriendly(),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
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

    LaunchedEffect(Unit) {
        detailsViewModel.getPokemon(pokemonName)
    }

    DetailsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackPressed = onBackPressed,
        onFavoritePressed = {
            detailsViewModel.favoritePokemon(
                name = pokemonName,
                isFavorite = it
            )
        }
    )
}