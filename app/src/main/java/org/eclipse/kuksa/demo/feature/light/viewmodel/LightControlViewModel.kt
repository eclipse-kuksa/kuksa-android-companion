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

package org.eclipse.kuksa.demo.feature.light.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.eclipse.kuksa.vss.VssLights
import org.eclipse.kuksa.vsscore.model.VssProperty

class LightControlViewModel : ViewModel() {
    var onClickToggleLight: (VssProperty<Boolean>) -> Unit = { }

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
