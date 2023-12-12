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
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.feature.door.view.DoorControlView
import org.eclipse.kuksa.companion.feature.door.viewModel.DoorControlViewModel
import org.eclipse.kuksa.companion.feature.light.view.LightControlView
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.companion.feature.temperature.view.TemperatureControlView
import org.eclipse.kuksa.companion.feature.temperature.viewmodel.TemperatureViewModel
import org.eclipse.kuksa.companion.feature.wheel.pressure.view.WheelPressureControlView
import org.eclipse.kuksa.companion.feature.wheel.pressure.viewmodel.WheelPressureViewModel

private const val TAB_INDEX_DOORS = 0
private const val TAB_INDEX_TEMPERATURE = 1
private const val TAB_INDEX_LIGHT = 2
private const val TAB_INDEX_WHEEL_PRESSURE = 3

private const val FAB_OFFSET_X = -10
private const val FAB_OFFSET_Y = 10

@Composable
fun TabRowHost(
    doorControlViewModel: DoorControlViewModel,
    temperatureViewModel: TemperatureViewModel,
    lightControlViewModel: LightControlViewModel,
    wheelPressureViewModel: WheelPressureViewModel,
    fabContent: @Composable () -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(TAB_INDEX_DOORS) }

    val tabData = listOf(
        TabData(R.drawable.baseline_sensor_door_24, "Doors"),
        TabData(R.drawable.baseline_device_thermostat_24, "Temperature"),
        TabData(R.drawable.baseline_light_mode_24, "Lights"),
        TabData(R.drawable.baseline_sports_volleyball_24, "Wheel Pressure"),
    )

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            tabData.forEachIndexed { index, tabData ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.height(50.dp),
                ) {
                    Icon(
                        painter = painterResource(id = tabData.drawableRes),
                        contentDescription = tabData.description,
                    )
                }
            }
        }

        Box {
            when (selectedTabIndex) {
                TAB_INDEX_DOORS -> DoorControlView(doorControlViewModel)
                TAB_INDEX_TEMPERATURE -> TemperatureControlView(temperatureViewModel)
                TAB_INDEX_LIGHT -> LightControlView(lightControlViewModel)
                TAB_INDEX_WHEEL_PRESSURE -> WheelPressureControlView(wheelPressureViewModel)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(FAB_OFFSET_X.dp, FAB_OFFSET_Y.dp),
            ) {
                fabContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    TabRowHost(
        DoorControlViewModel(Application()),
        TemperatureViewModel(),
        LightControlViewModel(),
        WheelPressureViewModel(),
    ) {
        FloatingActionButton(onClick = {}) {
            Image(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
            )
        }
    }
}

private data class TabData(
    @DrawableRes val drawableRes: Int,
    val description: String,
)
