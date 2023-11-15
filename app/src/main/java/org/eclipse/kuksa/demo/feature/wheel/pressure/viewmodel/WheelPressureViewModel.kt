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

package org.eclipse.kuksa.demo.feature.wheel.pressure.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.eclipse.kuksa.vss.VssAxle

class WheelPressureViewModel : ViewModel() {
    var axle by mutableStateOf(VssAxle())

    val pressureLeftFront by derivedStateOf {
        axle.row1.wheel.left.tire.pressure.value
    }

    val pressureRightFront by derivedStateOf {
        axle.row1.wheel.right.tire.pressure.value
    }

    val pressureLeftBack by derivedStateOf {
        axle.row2.wheel.left.tire.pressure.value
    }

    val pressureRightBack by derivedStateOf {
        axle.row2.wheel.right.tire.pressure.value
    }
}
