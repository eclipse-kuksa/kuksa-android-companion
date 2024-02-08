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

package org.eclipse.kuksa.companion.feature.home.view.navigation

import androidx.annotation.DrawableRes
import org.eclipse.kuksa.companion.R

enum class NavigationPage(
    @DrawableRes val iconRes: Int,
    val title: String,
    val description: String,
    val isSheetEnabled: Boolean,
) {
    DOORS(
        R.drawable.baseline_sensor_door_24,
        "Door Control",
        "Show status of the doors and trunk, (un-)lock and open/close them",
        true,
    ),
    TEMPERATURE(
        R.drawable.baseline_device_thermostat_24,
        "Temperature Control",
        "Check the temperature inside the car, increase or decrease the temperature. ",
        true,
    ),
    LIGHT(
        R.drawable.baseline_light_mode_24,
        "Light Control",
        "Check the status of the light, turn them on and off to check their functionality",
        true,
    ),
    WHEELS(
        R.drawable.baseline_sports_volleyball_24,
        "Wheel Pressure",
        "Check the pressure of the wheels",
        false,
    ),
    SETTINGS(
        R.drawable.baseline_settings_24,
        "Settings",
        "Settings",
        false,
    ),
}
