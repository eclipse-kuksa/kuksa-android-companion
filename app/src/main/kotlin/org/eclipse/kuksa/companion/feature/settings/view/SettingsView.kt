/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package org.eclipse.kuksa.companion.feature.settings.view

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.eclipse.kuksa.companion.feature.connection.model.ConnectionInfo
import org.eclipse.kuksa.companion.feature.connection.repository.ConnectionInfoRepository
import org.eclipse.kuksa.companion.feature.settings.viewModel.SettingsViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsView(
    settingsViewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
) {
    val connectionInfoState
        by settingsViewModel.connectionInfoFlow.collectAsStateWithLifecycle(initialValue = ConnectionInfo())

    var connectionInfo by remember(connectionInfoState) {
        mutableStateOf(connectionInfoState)
    }

    Scaffold(
        topBar = { TopBar(onNavigateBack) },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .consumeWindowInsets(paddingValues),
            ) {
                Category(label = "Connection")
                EditableTextSetting("Host", connectionInfo.host) { newValue ->
                    connectionInfo = connectionInfoState.copy(host = newValue)
                    settingsViewModel.updateConnectionInfo(connectionInfo)
                }

                EditableTextSetting("Port", connectionInfo.port.toString()) { newValue ->
                    try {
                        val port = newValue.toInt()
                        connectionInfo = connectionInfoState.copy(port = port)
                        settingsViewModel.updateConnectionInfo(connectionInfo)
                    } catch (e: NumberFormatException) {
                        // ignored gracefully
                    }
                }

                SwitchSetting("Enable TLS", connectionInfo.isTlsEnabled) { newValue ->
                    connectionInfo = connectionInfoState.copy(isTlsEnabled = newValue)
                    settingsViewModel.updateConnectionInfo(connectionInfo)
                }

                FileSelectorSetting(
                    label = "Certificate",
                    value = connectionInfo.certificate.uriPath,
                ) { uri: Uri? ->
                    val certificate = connectionInfoState.certificate.copy(uriPath = uri.toString())
                    connectionInfo = connectionInfoState.copy(certificate = certificate)
                    settingsViewModel.updateConnectionInfo(connectionInfo)
                }
            }
        }
    }
}

@Suppress("SameParameterValue") // re-usability
@Composable
private fun Category(label: String) {
    Row(
        modifier = Modifier.padding(10.dp),
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun EditableTextSetting(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .offset(10.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1F)
                .clickable {
                    isDialogOpen = true
                },
        ) {
            Text(text = label, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = value)
        }
    }

    if (isDialogOpen) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = contentColorFor(MaterialTheme.colorScheme.background).copy(alpha = 0.6f),
                )
                .clickable { isDialogOpen = true },
        ) {
            TextDialog(label, value, onClickOk = {
                isDialogOpen = false
                onValueChanged(it)
            }, onClickCancel = {
                isDialogOpen = false
            })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextDialog(
    label: String,
    value: String,
    onClickOk: (String) -> Unit,
    onClickCancel: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var newValue by remember { mutableStateOf(value) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = label)

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = newValue,
                onValueChange = { newValue = it },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onClickOk(newValue.trim())
                    },
                ),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
        ) {
            Button(onClick = {
                keyboardController?.hide()
                onClickCancel()
            }) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                keyboardController?.hide()
                onClickOk(newValue.trim())
            }) {
                Text("OK")
            }
        }
    }
}

@Suppress(
    "SameParameterValue", // re-usability
    "MagicNumber", // it does not make sense to create a constant for each elements weight
)
@Composable
private fun SwitchSetting(label: String, enabled: Boolean, onValueChanged: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(10.dp),
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .weight(3F)
                .align(CenterVertically),
        )
        Switch(checked = enabled, onCheckedChange = {
            onValueChanged(it)
        }, modifier = Modifier.weight(1F))
    }
}

@Suppress("SameParameterValue") // Reusability for the label parameter
@Composable
private fun FileSelectorSetting(
    label: String,
    value: String,
    onResult: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        val uri = it ?: return@rememberLauncherForActivityResult

        context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        onResult(uri)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .offset(10.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1F)
                .clickable {
                    val fileTypes = arrayOf("*/*")
                    launcher.launch(fileTypes)
                },
        ) {
            Text(text = label, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = value)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text("Settings") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }
        },
    )
}

@Preview
@Composable
private fun SettingsViewPreview() {
    val context = LocalContext.current
    val repository = ConnectionInfoRepository(context)
    val settingsViewModel = SettingsViewModel(repository)
    SettingsView(settingsViewModel) {
        // unused in preview
    }
}
