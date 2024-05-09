package com.gabrielgrs1.pokedex.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Loading(modifier: Modifier = Modifier, currentProgressLoading: Float) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = { currentProgressLoading },
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun LoadingPrev() {
    val currentProgress by remember {
        mutableFloatStateOf(2f)
    }
    Loading(currentProgressLoading = currentProgress)
}