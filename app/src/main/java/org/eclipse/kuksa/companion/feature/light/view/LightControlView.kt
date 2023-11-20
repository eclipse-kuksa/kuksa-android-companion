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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.R
import org.eclipse.kuksa.companion.extension.DarkGreen
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.vsscore.model.VssProperty

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LightControlView(viewModel: LightControlViewModel) {
    BottomSheetScaffold(
        sheetContent = { BottomSheetContent(viewModel) },
        containerColor = Color.White.copy(alpha = 0f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(paddingValues = it),
        ) {
            LightOverlay(viewModel)
        }
    }
}

@Composable
private fun BottomSheetContent(viewModel: LightControlViewModel) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    ) {
        Text(
            text = "Beam Lights",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterHorizontally),
        )

        Row {
            LightButton(viewModel, viewModel.isHighBeamLightOn, "High")
            LightButton(viewModel, viewModel.isLowBeamLightOn, "Low")
        }

        Text(
            text = "Direction Indicator Lights",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterHorizontally),
        )

        Row {
            LightButton(viewModel, viewModel.isDirectionIndicatorLeftSignaling, "Left")
            LightButton(viewModel, viewModel.isDirectionIndicatorRightSignaling, "Right")
        }

        Text(
            text = "Fog Lights",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterHorizontally),
        )

        Row {
            LightButton(viewModel, viewModel.isFogLightFrontOn, "Front")
            LightButton(viewModel, viewModel.isFogLightRearOn, "Rear")
        }

        Text(
            text = "Misc Lights",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterHorizontally),
        )

        Row {
            LightButton(viewModel, viewModel.isRunningLightOn, "Running")
            LightButton(viewModel, viewModel.isParkingLightOn, "Parking")
        }

        Row {
            LightButton(viewModel, viewModel.isHazardLightSignalling, "Hazard")
        }
    }
}

@Composable
private fun LightButton(
    viewModel: LightControlViewModel,
    vssProperty: VssProperty<Boolean>,
    text: String,
) {
    val containerColor = if (vssProperty.value) Color.DarkGreen else Color.Red

    Button(
        onClick = {
            viewModel.onClickToggleLight(vssProperty)
        },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp),
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(100.dp),
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

@Composable
private fun LightOverlay(viewModel: LightControlViewModel) {
    Column(modifier = Modifier.padding(10.dp)) {
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
            painterResource = R.drawable.lights_direction_indicator_24,
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

@Preview
@Composable
private fun LightControlViewPreview() {
    Surface {
        LightControlView(LightControlViewModel())
    }
}

@Preview
@Composable
private fun BottomSheetPreview() {
    Surface {
        BottomSheetContent(LightControlViewModel())
    }
}
