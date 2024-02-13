package org.eclipse.kuksa.companion.feature.settings.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Suppress("SameParameterValue") // re-usability
@Composable
fun CategorySetting(
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(SettingsElementPadding),
    ) {
        Text(
            text = label,
            fontSize = SettingsCategoryFontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun CategorySettingPreview() {
    Surface {
        CategorySetting(label = "Connection")
    }
}
