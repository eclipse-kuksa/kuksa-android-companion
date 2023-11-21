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

package org.eclipse.kuksa.companion.feature.home.view

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.eclipse.kuksa.companion.feature.connection.view.ConnectionStatusView
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.companion.feature.temperature.viewmodel.TemperatureViewModel
import org.eclipse.kuksa.companion.feature.wheel.pressure.viewmodel.WheelPressureViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    connectionStatusViewModel: ConnectionStatusViewModel,
    doorControlViewModel: DoorControlViewModel,
    temperatureViewModel: TemperatureViewModel,
    lightControlViewModel: LightControlViewModel,
    wheelPressureViewModel: WheelPressureViewModel,
    onNavigateToSettingsScreen: () -> Unit,
) {
    Scaffold(containerColor = Color.White.copy(alpha = 0f)) {
        Column(
            modifier = Modifier
                .consumeWindowInsets(it)
                .padding(it),
        ) {
            ConnectionStatusView(
                viewModel = connectionStatusViewModel,
            )
            TabRowHost(
                doorControlViewModel = doorControlViewModel,
                temperatureViewModel = temperatureViewModel,
                lightControlViewModel = lightControlViewModel,
                wheelPressureViewModel = wheelPressureViewModel,
            ) {
                FloatingActionButton(
                    onClick = { onNavigateToSettingsScreen() },
                ) {
                    Image(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    HomeScreen(
        connectionStatusViewModel = ConnectionStatusViewModel(),
        doorControlViewModel = DoorControlViewModel(Application()),
        temperatureViewModel = TemperatureViewModel(),
        lightControlViewModel = LightControlViewModel(),
        wheelPressureViewModel = WheelPressureViewModel(),
        onNavigateToSettingsScreen = { },
    )
}
