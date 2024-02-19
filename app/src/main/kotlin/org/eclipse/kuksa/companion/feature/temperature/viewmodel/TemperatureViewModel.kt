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

package org.eclipse.kuksa.companion.feature.temperature.viewmodel

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import org.eclipse.kuksa.VssSpecificationListener
import org.eclipse.kuksa.companion.extension.LightBlue
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.listener.FilteredVssSpecificationListener
import org.eclipse.kuksa.vss.VssHvac

private const val MIN_TEMP_OK = 17F
private const val MIN_TEMP_WARM = 25F

class TemperatureViewModel : ViewModel() {
    var vssTemperatureListener: VssSpecificationListener<VssHvac> =
        object : FilteredVssSpecificationListener<VssHvac>() {
            override fun onSpecificationChanged(vssSpecification: VssHvac) {
                hvac = vssSpecification
            }

            override fun onPostFilterError(throwable: Throwable) {
                Log.e(TAG, "Failed to subscribe to specification: $throwable")
            }
        }

    val minTemperature = 10
    val maxTemperature = 35

    var hvac by mutableStateOf(VssHvac())

    val temperatureDriverSideFront by derivedStateOf {
        hvac.station.row1.driver.temperature.value
    }

    val temperaturePassengerSideFront by derivedStateOf {
        hvac.station.row1.passenger.temperature.value
    }

    val temperatureDriverSideBack by derivedStateOf {
        hvac.station.row2.driver.temperature.value
    }
    val temperaturePassengerSideBack by derivedStateOf {
        hvac.station.row2.passenger.temperature.value
    }

    var plannedTemperature by mutableIntStateOf(minTemperature)

    var onPlannedTemperatureChanged: ((Int) -> Unit) = {}

    fun getTemperatureColor(currentTemperature: Int): Color {
        return when {
            currentTemperature >= MIN_TEMP_WARM -> Color.Red
            currentTemperature >= MIN_TEMP_OK -> Color.Green
            else -> Color.LightBlue
        }
    }
}
