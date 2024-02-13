/*
 * Copyright (c) 2023-2024 Contributors to the Eclipse Foundation
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

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.eclipse.kuksa.companion.extension.fetchFileName
import org.eclipse.kuksa.companion.feature.connection.model.ConnectionInfo
import org.eclipse.kuksa.companion.feature.connection.repository.ConnectionInfoRepository
import org.eclipse.kuksa.companion.feature.settings.viewModel.SettingsViewModel
import org.eclipse.kuksa.companion.ui.theme.KuksaCompanionTheme

val SettingsElementPadding = 10.dp
val SettingsTextPaddingEnd = 15.dp

val SettingsCategoryFontSize = 20.sp
val SettingPrimaryFontSize = 18.sp
val SettingSecondaryFontSize = 16.sp

@Composable
fun SettingsView(
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val connectionInfoState
        by settingsViewModel.connectionInfoFlow.collectAsStateWithLifecycle(initialValue = ConnectionInfo())

    var connectionInfo by remember(connectionInfoState) {
        mutableStateOf(connectionInfoState)
    }

    KuksaCompanionTheme {
        Surface(modifier) {
            Column(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
            ) {
                CategorySetting(label = "Connection")
                EditableTextSetting(
                    "Host",
                    connectionInfo.host,
                ) { newValue ->
                    connectionInfo = connectionInfoState.copy(host = newValue)
                    settingsViewModel.updateConnectionInfo(connectionInfo)
                }

                EditableTextSetting(
                    "Port",
                    connectionInfo.port.toString(),
                ) { newValue ->
                    try {
                        val port = newValue.toInt()
                        connectionInfo = connectionInfoState.copy(port = port)
                        settingsViewModel.updateConnectionInfo(connectionInfo)
                    } catch (e: NumberFormatException) {
                        // ignored gracefully
                    }
                }

                SwitchSetting(
                    "Enable TLS",
                    connectionInfo.isTlsEnabled,
                ) { newValue ->
                    connectionInfo = connectionInfoState.copy(isTlsEnabled = newValue)
                    settingsViewModel.updateConnectionInfo(connectionInfo)
                }

                if (connectionInfo.isTlsEnabled) {
                    val uri = connectionInfo.certificate.uri
                    val fileName = uri.fetchFileName(context) ?: "Select certificate..."

                    FileSelectorSetting(
                        label = "Certificate",
                        value = fileName,
                    ) { selectedUri: Uri? ->
                        val certificate = connectionInfoState.certificate.copy(uriPath = selectedUri.toString())
                        connectionInfo = connectionInfoState.copy(certificate = certificate)
                        settingsViewModel.updateConnectionInfo(connectionInfo)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsViewPreview() {
    val context = LocalContext.current
    val repository = ConnectionInfoRepository(context)
    val settingsViewModel = SettingsViewModel(repository)
    SettingsView(settingsViewModel)
}
