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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eclipse.kuksa.companion.extension.DarkGreen
import org.eclipse.kuksa.companion.feature.light.viewmodel.LightControlViewModel
import org.eclipse.kuksa.vsscore.model.VssProperty

@Composable
fun LightControlView(
    viewModel: LightControlViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
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

@Preview
@Composable
private fun LightControlViewPreview() {
    Surface {
        LightControlView(LightControlViewModel())
    }
}
