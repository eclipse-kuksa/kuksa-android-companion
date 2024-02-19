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

package org.eclipse.kuksa.companion.feature.light.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.application.PREVIEW_HEIGHT_DP
import org.eclipse.kuksa.companion.application.PREVIEW_WIDTH_DP
import org.eclipse.kuksa.companion.extension.windowSizeClass
import org.eclipse.kuksa.companion.feature.home.view.AdaptiveFlowColumnRow
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel

@Composable
fun LightOverlayView(
    viewModel: LightControlViewModel,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    AdaptiveFlowColumnRow(windowSizeClass, modifier = modifier.padding(10.dp)) {
        LightDashboardSymbol(
            isLightEnabled = viewModel.isHighBeamLightOn.value,
            painterResource = R.drawable.lights_beam_high_24,
            contentDescription = "High Beam Lights",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isLowBeamLightOn.value,
            painterResource = R.drawable.lights_beam_low_24,
            contentDescription = "Low Beam Lights",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isDirectionIndicatorSignaling,
            painterResource = viewModel.directionIndicatorRes,
            contentDescription = "Direction Indicator",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isFogLightFrontOn.value,
            painterResource = R.drawable.lights_fog_front_24,
            contentDescription = "Fog Lights Front",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isFogLightRearOn.value,
            painterResource = R.drawable.lights_fog_rear_24,
            contentDescription = "Fog Lights Rear",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isRunningLightOn.value,
            painterResource = R.drawable.lights_daylight_running_light_24,
            contentDescription = "Running Lights",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isParkingLightOn.value,
            painterResource = R.drawable.lights_parking_24,
            contentDescription = "Parking Lights",
        )
        LightDashboardSymbol(
            isLightEnabled = viewModel.isHazardLightSignalling.value,
            painterResource = R.drawable.lights_hazard_24,
            contentDescription = "Hazard Lights",
        )
    }
}

@Composable
private fun LightDashboardSymbol(
    isLightEnabled: Boolean,
    @DrawableRes painterResource: Int,
    contentDescription: String,
) {
    val color = if (isLightEnabled) Color.Green else Color.Red
    Image(
        painter = painterResource(id = painterResource),
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier.size(50.dp),
    )
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP)
@Preview(widthDp = PREVIEW_HEIGHT_DP, heightDp = PREVIEW_WIDTH_DP)
@Composable
private fun LightControlViewPreview() {
    val windowSizeClass = LocalConfiguration.current.windowSizeClass
    val viewModel = LightControlViewModel()
    Surface {
        LightOverlayView(viewModel, windowSizeClass)
    }
}
