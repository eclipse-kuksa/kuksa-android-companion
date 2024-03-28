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

package org.eclipse.kuksa.companion.feature.light.viewmodel

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.extension.TAG
import org.eclipse.kuksa.companion.listener.FilteredVssNodeListener
import org.eclipse.kuksa.connectivity.databroker.listener.VssNodeListener
import org.eclipse.kuksa.vss.VssLights
import org.eclipse.kuksa.vsscore.model.VssSignal

class LightControlViewModel : ViewModel() {
    var vssLightListener: VssNodeListener<VssLights> = object : FilteredVssNodeListener<VssLights>() {
        override fun onNodeChanged(vssNode: VssLights) {
            vssLight = vssNode
        }

        override fun onPostFilterError(throwable: Throwable) {
            Log.e(TAG, "Failed to subscribe to node: $throwable")
        }
    }

    var onClickToggleLight: (VssSignal<Boolean>) -> Unit = { }

    var vssLight: VssLights by mutableStateOf(VssLights())

    val isHighBeamLightOn by derivedStateOf {
        vssLight.beam.high.isOn
    }

    val isLowBeamLightOn by derivedStateOf {
        vssLight.beam.low.isOn
    }

    val isDirectionIndicatorLeftSignaling by derivedStateOf {
        vssLight.directionIndicator.left.isSignaling
    }

    val isDirectionIndicatorRightSignaling by derivedStateOf {
        vssLight.directionIndicator.right.isSignaling
    }

    val isDirectionIndicatorSignaling by derivedStateOf {
        isDirectionIndicatorLeftSignaling.value || isDirectionIndicatorRightSignaling.value
    }

    @get:DrawableRes
    val directionIndicatorRes by derivedStateOf {
        val isLeftSignalling = isDirectionIndicatorLeftSignaling.value
        val isRightSignalling = isDirectionIndicatorRightSignaling.value

        if (isLeftSignalling && isRightSignalling) {
            return@derivedStateOf R.drawable.lights_direction_indicator_both_24
        } else if (isLeftSignalling) {
            return@derivedStateOf R.drawable.lights_direction_indicator_left_24
        } else if (isRightSignalling) {
            return@derivedStateOf R.drawable.lights_direction_indicator_right_24
        }

        return@derivedStateOf R.drawable.lights_direction_indicator_none_24
    }

    val isFogLightFrontOn by derivedStateOf {
        vssLight.fog.front.isOn
    }

    val isFogLightRearOn by derivedStateOf {
        vssLight.fog.rear.isOn
    }

    val isHazardLightSignalling by derivedStateOf {
        vssLight.hazard.isSignaling
    }

    val isParkingLightOn by derivedStateOf {
        vssLight.parking.isOn
    }

    val isRunningLightOn by derivedStateOf {
        vssLight.running.isOn
    }
}
