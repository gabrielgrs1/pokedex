package com.gabrielgrs1.pokedex.presentation.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gabrielgrs1.pokedex.R


@Composable
fun Error() {
    Toast.makeText(
        LocalContext.current,
        stringResource(id = R.string.generic_error),
        Toast.LENGTH_LONG
    ).show()
}

@Preview(showBackground = true)
@Composable
private fun ErrorPrev() {
    Error()
}