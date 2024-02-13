@file:JvmName("FileSelectorSettingKt")

package org.eclipse.kuksa.companion.feature.settings.view

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.eclipse.kuksa.companion.R

@Suppress("SameParameterValue") // re-usability
@Composable
fun FileSelectorSetting(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onResult: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        val uri = it ?: return@rememberLauncherForActivityResult

        context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        onResult(uri)
    }

    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .padding(SettingsElementPadding)
            .clickable {
                val fileTypes = arrayOf("*/*")
                launcher.launch(fileTypes)
            },
    ) {
        val (labelRef, valueRef, imageRef) = createRefs()

        Text(
            text = label,
            fontSize = SettingPrimaryFontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = SettingsTextPaddingEnd)
                .constrainAs(labelRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(imageRef.start)
                },
        )
        Text(
            text = value,
            fontSize = SettingSecondaryFontSize,
            modifier = Modifier
                .padding(end = SettingsTextPaddingEnd)
                .constrainAs(valueRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(imageRef.start)
                    top.linkTo(labelRef.bottom)
                },
        )
        Image(
            painter = painterResource(id = R.drawable.baseline_upload_file_24),
            contentDescription = "Select Certificate",
            modifier = Modifier
                .size(40.dp)
                .constrainAs(imageRef) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
        )
    }
}

@Composable
@Preview
private fun SwitchSettingPreview() {
    Surface {
        FileSelectorSetting(label = "File Selector Setting", value = "Some Value") {
            // unused in preview
        }
    }
}
