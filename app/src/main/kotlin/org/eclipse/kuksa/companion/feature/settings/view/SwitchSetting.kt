package org.eclipse.kuksa.companion.feature.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Suppress("SameParameterValue") // re-usability
@Composable
fun SwitchSetting(
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onValueChanged: (Boolean) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onValueChanged(!enabled)
            }
            .padding(SettingsElementPadding),
    ) {
        val (textView, switchView) = createRefs()

        Text(
            text = label,
            fontSize = SettingPrimaryFontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = SettingsTextPaddingEnd)
                .constrainAs(textView) {
                    start.linkTo(parent.start)
                    end.linkTo(switchView.start)

                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
        )
        Switch(
            checked = enabled,
            onCheckedChange = {
                onValueChanged(it)
            },
            modifier = Modifier.constrainAs(switchView) {
                end.linkTo(parent.end)

                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
        )
    }
}

@Composable
@Preview
private fun SwitchSettingPreview() {
    Surface {
        SwitchSetting(label = "Switch Setting", enabled = true) {
            // unused in preview
        }
    }
}
