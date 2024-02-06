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

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Adds an adaptive FlowRow or FlowColumn depending on the [WindowWidthSizeClass] of the device.
 * If the device has a [WindowWidthSizeClass] of [WindowWidthSizeClass.Compact] a FlowColumn will
 * be used, while otherwise a FlowRow will be used.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdaptiveFlowColumnRow(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        FlowColumn(modifier) {
            content()
        }
    } else {
        FlowRow(modifier) {
            content()
        }
    }
}
