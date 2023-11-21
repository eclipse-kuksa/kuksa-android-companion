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

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.companion.feature.temperature.viewmodel.TemperatureViewModel
import org.eclipse.kuksa.companion.feature.wheel.pressure.viewmodel.WheelPressureViewModel

const val HomeScreen = "home"

@Suppress("LongParameterList") // calls compose code for which LongParameterList is disabled
fun NavGraphBuilder.homeScreen(
    connectionStatusViewModel: ConnectionStatusViewModel,
    doorControlViewModel: DoorControlViewModel,
    temperatureViewModel: TemperatureViewModel,
    lightControlViewModel: LightControlViewModel,
    wheelPressureViewModel: WheelPressureViewModel,
    onNavigateToSettingsScreen: () -> Unit,
) {
    composable(HomeScreen) {
        HomeScreen(
            connectionStatusViewModel = connectionStatusViewModel,
            doorControlViewModel = doorControlViewModel,
            temperatureViewModel = temperatureViewModel,
            lightControlViewModel = lightControlViewModel,
            wheelPressureViewModel = wheelPressureViewModel,
            onNavigateToSettingsScreen = onNavigateToSettingsScreen,
        )
    }
}

fun NavController.navigateToMainScreen(navOptions: NavOptions? = null) {
    navigate(HomeScreen, navOptions)
}
