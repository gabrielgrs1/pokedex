package com.gabrielgrs1.pokedex.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.gabrielgrs1.pokedex.core.formatToUserFriendly
import com.gabrielgrs1.pokedex.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers

@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color.Black),
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(Alignment.Center)
        ) {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.imageUrl)
                .dispatcher(Dispatchers.IO)
                .memoryCacheKey(pokemon.imageUrl)
                .diskCacheKey(pokemon.imageUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()

            AsyncImage(
                modifier = modifier
                    .size(160.dp)
                    .padding(12.dp),
                model = imageRequest,
                contentDescription = "image at ${pokemon.name}",
            )
        }

        Text(
            modifier = Modifier
                .padding(4.dp),
            text = pokemon.name.formatToUserFriendly(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PokemonListItemPrev() {
    PokemonListItem(
        Pokemon("Bulbasaur", "")
    )
}