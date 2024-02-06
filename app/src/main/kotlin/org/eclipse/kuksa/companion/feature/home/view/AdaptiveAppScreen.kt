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
import android.view.SurfaceHolder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.eclipse.kuksa.companion.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.extension.windowSizeClass
import org.eclipse.kuksa.companion.feature.connection.repository.ConnectionInfoRepository
import org.eclipse.kuksa.companion.feature.connection.view.AdaptiveConnectionStatusView
import org.eclipse.kuksa.companion.feature.connection.viewModel.ConnectionStatusViewModel
import org.eclipse.kuksa.companion.feature.door.view.DoorControlView
import org.eclipse.kuksa.companion.feature.door.view.DoorOverlayView
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.feature.home.view.navigation.AdaptiveNavigationView
import org.eclipse.kuksa.companion.feature.home.view.navigation.NavigationPage
import org.eclipse.kuksa.companion.feature.home.view.navigation.NavigationViewModel
import org.eclipse.kuksa.companion.feature.home.view.sheet.AdaptiveSheetView
import org.eclipse.kuksa.companion.feature.light.view.LightControlView
import org.eclipse.kuksa.companion.feature.light.view.LightOverlayView
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.companion.feature.settings.view.SettingsView
import org.eclipse.kuksa.companion.feature.settings.viewModel.SettingsViewModel
import org.eclipse.kuksa.companion.feature.temperature.view.TemperatureControlView
import org.eclipse.kuksa.companion.feature.temperature.view.TemperatureOverlayView
import org.eclipse.kuksa.companion.feature.temperature.viewmodel.TemperatureViewModel
import org.eclipse.kuksa.companion.feature.wheel.pressure.view.WheelPressureOverlayView
import org.eclipse.kuksa.companion.feature.wheel.pressure.viewmodel.WheelPressureViewModel

private const val ZINDEX_RAMSES_VIEW = 1F
private const val ZINDEX_OVERLAY = 2F
private const val ZINDEX_CONTROL = 3F

/**
 * Adds an adaptive AppScreen depending on the [WindowWidthSizeClass]. When the device has a [WindowWidthSizeClass] of
 * [WindowWidthSizeClass.Compact] all elements are placed on top of each other, while for devices with a higher class
 * the elements will be placed next to each other.
 */
@Composable
fun AdaptiveAppScreen(
    callback: SurfaceHolder.Callback,
    connectionStatusViewModel: ConnectionStatusViewModel,
    navigationViewModel: NavigationViewModel,
    doorControlViewModel: DoorControlViewModel,
    temperatureViewModel: TemperatureViewModel,
    lightControlViewModel: LightControlViewModel,
    wheelPressureViewModel: WheelPressureViewModel,
    settingsViewModel: SettingsViewModel,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    var selectedPage: NavigationPage by remember {
        mutableStateOf(navigationViewModel.selectedNavigationPage)
    }

    AdaptiveColumnRow(windowSizeClass = windowSizeClass, modifier = modifier.fillMaxSize()) {
        val context = LocalContext.current
        val resources = context.resources

        AdaptiveConnectionStatusView(connectionStatusViewModel, windowSizeClass)
        AdaptiveNavigationView(navigationViewModel, windowSizeClass) { page ->
            selectedPage = page
        }
        Box(modifier = Modifier.fillMaxSize()) {
            val paddingBottom = resources.getInteger(R.integer.RAMSES_VIEW_PADDING_BOTTOM)
            val paddingEnd = resources.getInteger(R.integer.RAMSES_VIEW_PADDING_END)
            RamsesView(
                callback = callback,
                modifier = Modifier
                    .zIndex(ZINDEX_RAMSES_VIEW)
                    .fillMaxSize()
                    // AdaptiveSheetPadding
                    .padding(bottom = paddingBottom.dp, end = paddingEnd.dp),
            )

            val overlayModifier = Modifier
                .zIndex(ZINDEX_OVERLAY)
                .fillMaxSize()
                // AdaptiveSheetPadding
                .padding(bottom = paddingBottom.dp, end = paddingEnd.dp)

            val controlModifier = Modifier
                .zIndex(ZINDEX_CONTROL)
                .fillMaxSize()

            when (selectedPage) {
                NavigationPage.DOORS -> {
                    DoorOverlayView(doorControlViewModel, windowSizeClass, overlayModifier)
                    AdaptiveSheetView(windowSizeClass, controlModifier) {
                        DoorControlView(doorControlViewModel)
                    }
                }

                NavigationPage.TEMPERATURE -> {
                    TemperatureOverlayView(temperatureViewModel, windowSizeClass, overlayModifier)
                    AdaptiveSheetView(windowSizeClass, controlModifier) {
                        TemperatureControlView(temperatureViewModel)
                    }
                }

                NavigationPage.LIGHT -> {
                    LightOverlayView(lightControlViewModel, windowSizeClass, overlayModifier)
                    AdaptiveSheetView(windowSizeClass, controlModifier) {
                        LightControlView(lightControlViewModel)
                    }
                }

                NavigationPage.WHEELS -> {
                    WheelPressureOverlayView(wheelPressureViewModel, windowSizeClass, overlayModifier)
                    AdaptiveSheetView(windowSizeClass) {
                        WheelPressureOverlayView(wheelPressureViewModel, windowSizeClass)
                    }
                }

                NavigationPage.SETTINGS -> SettingsView(settingsViewModel, overlayModifier)
            }
        }
    }
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
@Composable
private fun AdaptiveAppScreenPreview() {
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            // unused
        }

        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int,
        ) {
            // unused
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // unused
        }
    }

    val application = Application()
    val context = LocalContext.current
    val repository = ConnectionInfoRepository(context)
    AdaptiveAppScreen(
        callback,
        ConnectionStatusViewModel(),
        NavigationViewModel(),
        DoorControlViewModel(application),
        TemperatureViewModel(),
        LightControlViewModel(),
        WheelPressureViewModel(),
        SettingsViewModel(repository),
        windowSizeClass,
    )
}